@file:Suppress("NonAsciiCharacters", "SpellCheckingInspection", "MemberVisibilityCanBePrivate")

package dev.eastar.coroutine.demo

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors
import java.util.concurrent.Semaphore
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class UnitTestWithCoroutine {

    fun asyncFun(callback: (Int) -> Unit) {
        Executors.newSingleThreadExecutor().execute {
            println("  work thread start")
            Thread.sleep(1000)
            callback(2 + 2)
            println("  work thread end")
        }
    }

    @Test
    fun `Bad case 1 must fail`() {
        println("main thread start")

        asyncFun {
            println("main thread check assert") //이 로그는 확인 할 수가 없어요
            assertEquals(3, it)
        }

        println("main thread end")
    }

    @Test
    fun `Bad case 2 must success`() {
        println("main thread start")
        var result = -1
        asyncFun {
            println("main thread check assert") //이 로그는 확인 할 수가 없어요
            result = it
        }
        assertEquals(4, result)

        println("main thread end")
    }

    @Test
    fun `use Semaphore를 이용한 동기화 사용`() {
        println("main thread start")
        val semaphore = Semaphore(0)
        var result = -1

        asyncFun {
            result = it
            semaphore.release()
        }
        println("main thread check assert")
        semaphore.acquire()
        assert(4 == result)
        println("main thread end")
    }

    @Test
    fun `use suspendCoroutine`() = runBlocking {
        println("main thread start")
        val result = suspendCoroutine<Int> { continuation ->
            asyncFun {
                continuation.resume(it)
            }
        }
        println("main thread check assert")
        assertEquals(4, result)
        println("main thread end")
    }

    @Test
    fun `use suspendCoroutine final`() {
        println("main thread start")
        val result = block<Int> { continuation ->
            asyncFun {
                continuation.resume(it)
            }
        }
        println("main thread check assert")
        assertEquals(4, result)
        println("main thread end")
    }

    fun <T> block(block: (Continuation<T>) -> Unit): T? {
        var result: T? = null
        runBlocking {
            result = suspendCoroutine<T> {
                block(it)
            }
        }
        return result
    }

    @Test
    fun `use Channel 1`() = runBlocking {
        println("main thread start")
        val channel = Channel<Int>()
        Executors.newSingleThreadExecutor().execute {
            launch {
                println("  work thread start")
                channel.send(2 + 2)
                println("  work thread end")
            }
        }
        println("main thread check assert")
        val result = channel.receive()
        assertEquals(4, result)
        println("main thread end")
    }

    @Test
    fun `use Channel final`() = runBlocking {
        println("main thread start")
        val channel = Channel<Int>()

        asyncFun {
            launch { channel.send(it) }
        }

        println("main thread check assert")
        val result = channel.receive()
        assertEquals(4, result)
        println("main thread end")
    }

    @Test
    fun `unit test에서는 잘도는 network 작업`() {
        println("main thread start")

        var url = "http://127.0.0.1/veryBigFile.iso"
        val conn = (URL(url).openConnection() as HttpURLConnection)
        val responseCode = conn.responseCode
        println("main thread check assert step 1")
        assertEquals(200, responseCode)

        val os = ByteArrayOutputStream(1024 * 10)
        val size = conn.inputStream.copyTo(os)
        os.close()
        println("main thread check assert step 2")
        assertEquals(5508, size)

        println("main thread end")
    }

}
