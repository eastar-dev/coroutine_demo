@file:Suppress("LocalVariableName")

package dev.eastar.coroutine.demo

import android.app.Application
import android.log.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MapsViewModel(application: Application) : AndroidViewModel(application) {

    fun runBlockingTest(view: View) {
        Log.e()
        runBlocking2(view)
        Log.e()
    }

    fun runBlocking2(view: View) = runBlocking {
        Log.e()
//        launch {
//            Log.e()
            delay(10000)
//            Log.e()
//        }
        Log.e()
    }

//    fun runBlockingTest(view: View) = runBlocking {
//        Log.e()
////        delay(10000)
//        launch {
//            delay(10000)
//        }
//    }

}
