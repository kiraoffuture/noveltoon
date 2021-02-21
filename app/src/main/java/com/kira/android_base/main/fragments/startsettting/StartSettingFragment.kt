package com.kira.android_base.main.fragments.startsettting

import android.util.Log
import com.kira.android_base.R
import com.kira.android_base.base.ui.BaseFragment
import com.kira.android_base.main.fragments.startsettting.languageselector.Language
import com.kira.android_base.main.fragments.startsettting.languageselector.LanguageSelectorDialog

class StartSettingFragment : BaseFragment(R.layout.fragment_start_setting) {

    companion object {
        val TAG: String = StartSettingFragment::class.java.simpleName
    }

    override fun initViews() {
        context?.let {
            LanguageSelectorDialog(it, object : LanguageSelectorDialog.Listener {
                override fun onLanguageSelected(language: Language) {
                    Log.d(TAG, "onLanguageSelected: $language")
                }
            }).show()
        }
    }
}
