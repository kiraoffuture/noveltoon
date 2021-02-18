package com.kira.android_base.main.fragments.startsettting

import android.util.Log
import com.kira.android_base.R
import com.kira.android_base.base.ui.BaseFragment

class StartSettingFragment : BaseFragment(R.layout.fragment_start_setting) {

    companion object {
        val TAG: String = StartSettingFragment::class.java.simpleName
    }

    override fun initViews() {
        context?.let {
            YearSelectorDialog(it, object : YearSelectorDialog.Listener {
                override fun onDone(year: Int) {
                    Log.d(TAG, "onDone: year = $year")
                }
            }).show()
        }
    }
}
