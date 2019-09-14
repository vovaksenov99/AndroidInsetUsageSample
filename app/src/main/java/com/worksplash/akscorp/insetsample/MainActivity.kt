package com.worksplash.akscorp.insetsample

import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.android.synthetic.main.activity_main.button
import kotlinx.android.synthetic.main.activity_main.is_keyboard_open
import kotlinx.android.synthetic.main.activity_main.root

class MainActivity : AppCompatActivity() {

    lateinit var insetsUtils: InsetUtils
    var isEdgeToEdge = false

    val keyboardStateChangeListener: (Boolean, WindowInsetsCompat) -> Unit = { isKeyboardOpen: Boolean, windowInsets: WindowInsetsCompat ->
        when {
            isEdgeToEdge -> {
                root.setPadding(
                        0,
                        windowInsets.systemWindowInsetTop,
                        0,
                        windowInsets.systemWindowInsetBottom
                )
            }
            !isEdgeToEdge && !isKeyboardOpen -> {
                root.setPadding(
                        0,
                        0,
                        0,
                        0
                )
            }
        }
        is_keyboard_open.text = "Keyboard ${if (isKeyboardOpen) "open" else "close"}"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        insetsUtils = InsetUtils(resources.configuration.orientation)
        insetsUtils.setKeyboardStateChangeListener(window.decorView, keyboardStateChangeListener)

        isEdgeToEdge = savedInstanceState?.getBoolean("isEdgeToEdge") ?: false
        setEdgeToEdgeEnable(isEdgeToEdge)

        button.setOnClickListener {
            isEdgeToEdge = !isEdgeToEdge
            recreate()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("isEdgeToEdge", isEdgeToEdge)
        super.onSaveInstanceState(outState)
    }

    fun setEdgeToEdgeEnable(isEnable: Boolean) {
        with(insetsUtils) {
            if (isEnable) {
                statusBarHeight = 0
                navigationBarHeight = 0

                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    root.setPadding(
                            0,
                            startStatusBarHeight,
                            0,
                            startNavigationBarHeight
                    )
                } else {
                    root.setPadding(
                            0,
                            startStatusBarHeight,
                            startNavigationBarHeight,
                            0
                    )
                }

                window.navigationBarColor = Color.TRANSPARENT
                window.statusBarColor = Color.TRANSPARENT

            } else {
                statusBarHeight = startStatusBarHeight
                navigationBarHeight = startNavigationBarHeight

                window.navigationBarColor = Color.BLACK
                window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.colorPrimaryDark)

                root.setPadding(
                        0,
                        0,
                        0,
                        0
                )
            }
        }
        window.decorView.requestApplyInsets()
    }

}
