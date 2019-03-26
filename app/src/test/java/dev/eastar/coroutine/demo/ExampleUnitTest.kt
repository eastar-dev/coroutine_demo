package dev.eastar.coroutine.demo

import android.log.Log
import io.reactivex.Observable
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun concatTest() {
        Log.MODE = Log.eMODE.SYSTEMOUT
        assertEquals("ABCDEredyellowblue", block())
    }
}

fun block(): String? {
    Log.e(1)
//    var result: T? = null
    var sss: String? = null
    runBlocking {
        Log.e(2)
        sss = suspendCoroutine<String> { continuation ->
            Log.e(3)
            val resultString = StringBuilder()
            val shapes = listOf("A", "B", "C", "D", "E")
            val colorList = listOf("red", "yellow", "blue")
            val source: Observable<String> = Observable.concat(
                    Observable.interval(0L, 30L, TimeUnit.MILLISECONDS)
                            .map { index -> shapes[index.toInt()] }
                            .take(shapes.size.toLong()),
                    Observable.interval(0L, 100L, TimeUnit.MILLISECONDS)
                            .map { index -> colorList[index.toInt()] }
                            .take(colorList.size.toLong())
            )
            source.subscribe({
                Log.e(4)
                resultString.append(it)
                println("${Thread.currentThread()} $it")
            }, {
                Log.e(5)
            }, {
                Log.e(6)
                continuation.resume(resultString.toString())
            })
        }
        Log.e(7)
    }
    Log.e(8)
//    return result
    return sss
}
