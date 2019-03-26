package dev.eastar.coroutine.demo

import android.log.Log
import io.reactivex.Observable
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTestWithCoroutine {

    @Test
    fun concatTest() {
        Log.MODE = Log.eMODE.SYSTEMOUT

        val result = waitResult<String> {
            val resultString = StringBuilder()
            val shapes = listOf("A", "B", "C", "D", "E")
            val colorList = listOf("red", "yellow", "blue")
            Observable.concat(
                    Observable.interval(0L, 30L, TimeUnit.MILLISECONDS)
                            .map { index -> shapes[index.toInt()] }
                            .take(shapes.size.toLong()),
                    Observable.interval(0L, 100L, TimeUnit.MILLISECONDS)
                            .map { index -> colorList[index.toInt()] }
                            .take(colorList.size.toLong()))
                    .subscribe({
                        resultString.append(it)
                        println("${Thread.currentThread()} $it")
                    }, {

                    }, {
                        it.resume(resultString.toString())
                    })
        }

        assertEquals("ABCDEredyellowblue", result)
    }

    @Test
    fun simpleSample() {
        Log.MODE = Log.eMODE.SYSTEMOUT
        val result = waitResult<Int> {
            Executors.newSingleThreadExecutor().execute {
                it.resume(2 + 2)
            }
        }
        assertEquals(4, result)
    }
}

fun <T> waitResult(block: (Continuation<T>) -> Unit): T? {
    var result: T? = null
    runBlocking {
        result = suspendCoroutine<T> {
            block(it)
        }
    }
    return result
}
