package com.kira.android_base.main

import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.kira.android_base.R
import com.kira.android_base.base.ui.BaseFragment
import com.kira.android_base.databinding.ActivityMainBinding
import com.kira.android_base.main.fragments.login.LoginFragment
import com.kira.android_base.main.fragments.splash.SplashFragment
import com.kira.android_base.main.fragments.startsettting.StartSettingFragment

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private var activityMainBinding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViews()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val isConsumed = super.dispatchTouchEvent(ev)
        if (ev?.action == MotionEvent.ACTION_DOWN) {
            currentFocus?.let { it ->
                if (it !is EditText) return@let
                val outRect = Rect()
                it.getGlobalVisibleRect(outRect)
                if (!outRect.contains(ev.rawX.toInt(), ev.rawY.toInt())) {
                    it.clearFocus()
                }
            }
        }
        return isConsumed
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    private fun initViews() {
        activityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        manageActionBarFollowFragment()

        openScreen(SplashFragment.TAG)
    }

    fun openScreen(
        fragmentTag: String,
        fragment: BaseFragment? = null,
        isAddToBackStack: Boolean = true,
        backStackName: String? = null
    ) {
        val transaction = supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .apply {
                supportFragmentManager.primaryNavigationFragment?.let { currentFragment ->
                    val currentFragmentTag = currentFragment::class.java.simpleName
                    if (fragmentTag == currentFragmentTag) return
                    hide(currentFragment)
                }
                fragment?.let {
                    add(R.id.container, it, it::class.java.simpleName)
                        .setPrimaryNavigationFragment(it)
                        .apply {
                            if (isAddToBackStack) addToBackStack(backStackName)
                        }
                        .commit()
                    return
                }
            }

        var destinationFragment = supportFragmentManager.findFragmentByTag(fragmentTag)
        if (destinationFragment != null) {
            transaction.show(destinationFragment)
        } else {
            destinationFragment = generateNewFragmentWithTag(fragmentTag) ?: return
            transaction.add(
                R.id.container,
                destinationFragment,
                destinationFragment::class.java.simpleName
            )
        }

        transaction.setPrimaryNavigationFragment(destinationFragment)
            .apply {
                if (isAddToBackStack) addToBackStack(backStackName)
            }
            .commit()
    }

    private fun generateNewFragmentWithTag(fragmentTag: String): Fragment? {
        return when (fragmentTag) {
            LoginFragment.TAG -> {
                LoginFragment()
            }
            SplashFragment.TAG -> {
                SplashFragment()
            }
            StartSettingFragment.TAG -> {
                StartSettingFragment()
            }
            else -> null
        }
    }

    private fun manageActionBarFollowFragment() {
        supportFragmentManager.addOnBackStackChangedListener {
            when (supportFragmentManager.primaryNavigationFragment) {
                is SplashFragment, is StartSettingFragment -> {
                    supportActionBar?.hide()
                }
            }
        }
    }

    fun makeStatusBarTransparent(isLightStatusBar: Boolean = true) {
        window?.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            decorView.systemUiVisibility =
                if (isLightStatusBar) View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                else View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            statusBarColor = Color.TRANSPARENT
        }
    }

    fun marginTopAfterFullScreen(view: View?) {
        ViewCompat.setOnApplyWindowInsetsListener(
            activityMainBinding?.container ?: return
        ) { _, insets ->
            view?.apply {
                setPadding(
                    paddingLeft,
                    paddingTop + insets.systemWindowInsetTop,
                    paddingRight,
                    paddingBottom
                )
            }
            insets.consumeSystemWindowInsets()
        }
    }

    fun hideStatusBar(isFitsSystemWindows: Boolean = true) {
        WindowCompat.setDecorFitsSystemWindows(window, isFitsSystemWindows)
        WindowInsetsControllerCompat(
            window,
            activityMainBinding?.root ?: return
        ).let { controller ->
            controller.hide(WindowInsetsCompat.Type.statusBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    fun showStatusBar(isFitsSystemWindows: Boolean = false) {
        WindowCompat.setDecorFitsSystemWindows(window, isFitsSystemWindows)
        WindowInsetsControllerCompat(
            window,
            activityMainBinding?.root ?: return
        ).show(WindowInsetsCompat.Type.statusBars())
    }

    fun addOneTimeBackStackListener(onBackStackChange: () -> Unit) {
        supportFragmentManager.apply {
            addOnBackStackChangedListener(object :
                FragmentManager.OnBackStackChangedListener {
                override fun onBackStackChanged() {
                    onBackStackChange()
                    removeOnBackStackChangedListener(this)
                }
            })
        }
    }

    fun popBackStack() = supportFragmentManager.popBackStack()
}
