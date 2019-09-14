package com.worksplash.akscorp.insetsample

import android.content.res.Configuration
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class InsetUtils(val orientation: Int) {

    var statusBarHeight = -1
    var navigationBarHeight = -1

    var startNavigationBarHeight = -1
        private set

    var startStatusBarHeight = -1
        private set

    fun setKeyboardStateChangeListener(view: View, listener: (Boolean, WindowInsetsCompat) -> Unit) {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->
            val bottomInset = windowInsets.systemWindowInsetBottom
            val rightInset = windowInsets.systemWindowInsetRight
            val topInset = windowInsets.systemWindowInsetTop

            if (startNavigationBarHeight == -1){
                startNavigationBarHeight = if(orientation == Configuration.ORIENTATION_PORTRAIT)
                    bottomInset
                else
                    rightInset
            }
            if (startStatusBarHeight == -1) startStatusBarHeight = topInset


            if(orientation == Configuration.ORIENTATION_PORTRAIT){
                listener(isKeyboardEnable(bottomInset), windowInsets)
                applyWindowsInsets(
                        view,
                        windowInsets,
                        top = if (statusBarHeight == -1) topInset else statusBarHeight,
                        bottom = if (navigationBarHeight == -1) bottomInset else navigationBarHeight
                )
            }
            else{
                listener(isKeyboardEnable(rightInset), windowInsets)
                applyWindowsInsets(
                        view,
                        windowInsets,
                        top = if (statusBarHeight == -1) topInset else statusBarHeight,
                        right = if (navigationBarHeight == -1) rightInset else navigationBarHeight
                )
            }

        }
    }

    fun applyWindowsInsets(view: View, insets: WindowInsetsCompat, left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): WindowInsetsCompat? {
        return ViewCompat.onApplyWindowInsets(
                view,
                insets.replaceSystemWindowInsets(
                        left,
                        top,
                        right,
                        bottom
                )
        )
    }

    private fun isKeyboardEnable(navigationHeight: Int) = navigationHeight != startNavigationBarHeight && startNavigationBarHeight != -1

}
