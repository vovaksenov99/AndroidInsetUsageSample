package com.worksplash.akscorp.insetsample

import android.content.res.Configuration
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

/**
 *     This detector NOT detect landscape fullscreen keyboard
 *
 *     Your activity must have android:windowSoftInputMode="adjustResize" at least, otherwise listeners won't be called
 */
class KeyboardBehaviorDetector(
        private val activity: AppCompatActivity
) : LifecycleObserver {

    private val view = activity.window.decorView

    // -1 when we never measure insets yet
    var statusBarHeight = -1
    var navigationBarHeight = -1

    var startNavigationBarHeight = -1
        private set

    var startStatusBarHeight = -1
        private set

    var listener: ((Boolean, WindowInsetsCompat) -> Unit)? = null

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun startDetection() {
        ViewCompat.setOnApplyWindowInsetsListener(view) { _, windowInsets ->
            val orientation = activity.resources.configuration.orientation

            val bottomInset = windowInsets.systemWindowInsetBottom
            val rightInset = windowInsets.systemWindowInsetRight
            val topInset = windowInsets.systemWindowInsetTop

            if (startNavigationBarHeight == -1) {
                startNavigationBarHeight = if (orientation == Configuration.ORIENTATION_PORTRAIT)
                    bottomInset
                else
                    rightInset
            }

            if (startStatusBarHeight == -1) startStatusBarHeight = topInset


            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                listener?.invoke(isKeyboardOpen(bottomInset), windowInsets)
                applyWindowsInsets(
                        view,
                        windowInsets,
                        top = if (statusBarHeight == -1) topInset else statusBarHeight,
                        bottom = if (navigationBarHeight == -1) bottomInset else navigationBarHeight
                )
            } else {
                listener?.invoke(isKeyboardOpen(bottomInset), windowInsets)
                applyWindowsInsets(
                        view,
                        windowInsets,
                        top = if (statusBarHeight == -1) topInset else statusBarHeight,
                        right = if (navigationBarHeight == -1) rightInset else navigationBarHeight
                )
            }
        }
        ViewCompat.requestApplyInsets(view)
    }

    private fun applyWindowsInsets(view: View, insets: WindowInsetsCompat, left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): WindowInsetsCompat? {
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

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopDetection() {
        ViewCompat.setOnApplyWindowInsetsListener(view, null)
    }

    private fun isKeyboardOpen(navigationHeight: Int) =
            navigationHeight != startNavigationBarHeight && startNavigationBarHeight != -1

}