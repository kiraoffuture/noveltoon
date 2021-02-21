package com.kira.android_base.main.fragments.startsettting.languageselector

import android.content.Context
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kira.android_base.R
import com.kira.android_base.base.supports.utils.RIPPLE_COMPLETELY_DELAY
import com.kira.android_base.base.ui.widgets.recyclerview.BaseRecyclerViewAdapter
import com.kira.android_base.databinding.DialogLanguageSelectorBinding
import com.kira.android_base.databinding.ItemLanguageBinding
import java.util.*

class LanguageSelectorDialog(
    private val context: Context,
    private val listener: Listener
) {

    private var bottomSheetDialog: BottomSheetDialog? = null
    private val languages = context.resources.getStringArray(R.array.language_codes)
        .zip(context.resources.getStringArray(R.array.languages))
        .map {
            Language(
                it.first,
                it.second,
                Locale.getDefault().language == it.first
            )
        }

    fun show() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = BottomSheetDialog(context).apply {
                val binding: DialogLanguageSelectorBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(context),
                    R.layout.dialog_language_selector,
                    null,
                    false
                )
                setContentView(binding.root)
                binding.languageSelectorDialog = this@LanguageSelectorDialog
                binding.rvLanguage.apply {
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                    adapter = object : BaseRecyclerViewAdapter<Language>(R.layout.item_language) {
                        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                            (holder.viewDataBinding as ItemLanguageBinding?)?.apply {
                                itemLanguage = list[position]
                                this.itemPosition = position
                                this.adapterListener = listener
                                executePendingBindings()
                            }
                        }
                    }.apply {
                        list.addAll(languages)
                        listener = object : BaseRecyclerViewAdapter.Listener<Language> {
                            override fun onItemClick(position: Int, t: Language) {
                                super.onItemClick(position, t)
                                list.forEachIndexed { index, language ->
                                    language.isSelected = index == position
                                }
                                postDelayed({ notifyDataSetChanged() }, RIPPLE_COMPLETELY_DELAY)
                            }
                        }
                    }
                    addItemDecoration(
                        DividerItemDecoration(
                            context,
                            LinearLayoutManager.VERTICAL
                        )
                    )
                }
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        bottomSheetDialog?.show()
    }

    fun onLanguageSelected() {
        bottomSheetDialog?.window?.decorView?.postDelayed({
            bottomSheetDialog?.dismiss()
            listener.onLanguageSelected(languages.find { it.isSelected } ?: return@postDelayed)
        }, RIPPLE_COMPLETELY_DELAY)
    }

    interface Listener {
        fun onLanguageSelected(language: Language)
    }
}
