package com.kira.android_base.main.fragments.splash

import com.kira.android_base.R
import com.kira.android_base.base.ui.BaseFragment
import com.kira.android_base.databinding.FragmentSplashBinding
import com.kira.android_base.main.fragments.startsettting.StartSettingFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    companion object {
        val TAG: String = SplashFragment::class.java.simpleName
    }

    override fun initViews() {
        mainActivity?.hideStatusBar()
        viewDataBinding?.root?.postDelayed({
            mainActivity?.addOneTimeBackStackListener {
                mainActivity?.openScreen(StartSettingFragment.TAG)
            }
            mainActivity?.popBackStack()
        }, 1000L)

        //fake data
        (viewDataBinding as FragmentSplashBinding?)?.imageUrl =
            "https://static.iluvk.vn/uploads/images/newscontent/2019/11/08/news_content_157319547350.jpg"
    }
}
