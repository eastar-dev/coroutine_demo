package dev.eastar.coroutine.demo

import android.content.Context
import android.content.res.Resources
import android.widget.Toast

fun Context.toast(text: CharSequence) {
    Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
}

val Int.dp: Int get() = (this * Resources.getSystem().displayMetrics.density).toInt()
