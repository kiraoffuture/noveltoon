package com.kira.android_base.main.fragments.splash

import com.kira.android_base.R
import com.kira.android_base.base.ui.BaseFragment
import com.kira.android_base.databinding.FragmentSplashBinding
import com.kira.android_base.main.fragments.startsettting.StartSettingFragment

class SplashFragment : BaseFragment(R.layout.fragment_splash) {

    companion object {
        val TAG: String = SplashFragment::class.java.simpleName
        private const val SPLASH_DELAY = 5000L
    }

    override fun initViews() {
        mainActivity?.hideStatusBar()
        viewDataBinding?.root?.postDelayed({
            mainActivity?.openScreen(StartSettingFragment.TAG, isAddToBackStack = false)
        }, SPLASH_DELAY)

        //fake data
        (viewDataBinding as FragmentSplashBinding?)?.imageUrl =
            "https://static.iluvk.vn/uploads/images/newscontent/2019/11/08/news_content_157319547350.jpg"
    }
}
