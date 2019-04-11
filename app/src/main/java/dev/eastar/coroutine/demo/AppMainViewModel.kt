@file:Suppress("LocalVariableName")

package dev.eastar.coroutine.demo

import android.app.Application
import android.content.Intent
import android.view.View
import androidx.lifecycle.AndroidViewModel

class AppMainViewModel(application: Application) : AndroidViewModel(application) {
    fun testUIthreadrunBlocking(view: View) = view.context.startActivity(Intent(view.context, MapsActivity::class.java))
    fun testDownloadWorkBefore(view: View) = view.context.startActivity(Intent(view.context, DownloadWorkBefore::class.java))
    fun testDownloadWorkAfter(view: View) = view.context.startActivity(Intent(view.context, DownloadWorkAfter::class.java))
    fun testFirebaseBefore(view: View) = view.context.startActivity(Intent(view.context, FirebaseBefore::class.java))
    fun testFirebaseAfter(view: View) = view.context.startActivity(Intent(view.context, FirebaseAfter::class.java))
    fun testIntroBefore(view: View) = view.context.startActivity(Intent(view.context, IntroBefore::class.java))
    fun testIntroAfter(view: View) = view.context.startActivity(Intent(view.context, IntroAfter::class.java))
}