package dev.eastar.coroutine.etc

import android.app.Dialog
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle

open class BaseActivity : AppCompatActivity() {
    private var destroied: Boolean = false
    override fun onDestroy() {
        super.onDestroy()
        destroied = true
    }

    private val progress by lazy { createProgress() }

    open fun createProgress(): Dialog {
        return Dialog(this).apply {
            setContentView(ProgressBar(context, null, android.R.attr.progressBarStyleLarge), FrameLayout.LayoutParams(60.dp, 60.dp).apply { gravity = Gravity.CENTER })
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            setCanceledOnTouchOutside(false)
            setCancelable(true)
        }
    }

    fun showProgress() {
        if (lifecycle.currentState === Lifecycle.State.DESTROYED) return
        if (isFinishing) return
        if (destroied) return
        if (!progress.isShowing) {
            progress.show()
        }
    }

    fun dismissProgress() {
        if (progress.isShowing) progress.dismiss()
    }
}
