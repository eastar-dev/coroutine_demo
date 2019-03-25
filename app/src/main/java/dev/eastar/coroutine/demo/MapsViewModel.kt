@file:Suppress("LocalVariableName")

package dev.eastar.coroutine.demo

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class MapsViewModel(application: Application) : AndroidViewModel(application) {
    fun runBlockingTest(view: View) = runBlocking {
        delay(10000)
    }
}
