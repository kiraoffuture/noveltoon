package com.kira.android_base.main.fragments.startsettting

import android.app.Dialog
import android.content.Context
import androidx.databinding.DataBindingUtil
import com.kira.android_base.R
import com.kira.android_base.databinding.DialogStartSettingYearSelectorBinding

class YearSelectorDialog(
    private val context: Context,
    private val listener: Listener
) {

    private var dialog: Dialog? = null

    fun show() {
        if (dialog == null) {
            dialog = Dialog(context).apply {
                setCancelable(false)
                val binding: DialogStartSettingYearSelectorBinding =
                    DataBindingUtil.inflate(
                        layoutInflater,
                        R.layout.dialog_start_setting_year_selector,
                        null,
                        false
                    )
                setContentView(binding.root)
                binding.yearSelectorDialog = this@YearSelectorDialog
            }
        }
        try {
            dialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun done(year: Int) {
        dialog?.dismiss()
        listener.onDone(year)
    }

    interface Listener {
        fun onDone(year: Int)
    }
}
