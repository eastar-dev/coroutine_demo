package dev.eastar.coroutine.demo

import android.content.Context
import android.content.Intent
import android.log.Log
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.internal.ConnectionErrorMessages.getAppName
import dev.eastar.coroutine.etc.toast
import kotlinx.coroutines.*
import kotlin.coroutines.resume

class IntroAfter : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Button(this).apply {
            setOnClickListener { load() }
            text = "start intro after"
        })
    }

    //test stateus
    companion object {
        val IS_ROOTING = false
        val NOT_INSTALLED_V3 = false
        val HAS_EVENT = true
    }

    fun load() = CoroutineScope(Dispatchers.Main).launch {
        Log.e(0)
        toast("rootingChecked : start")
        step00RootingCheck()
        toast("rootingChecked : end")
        Log.e(1)
        toast("step01v3Check : start")
        step01v3Check()
        toast("step01v3Check : end")
        Log.e(2)
        val deferredGcm = async {
            toast("step04gcmCheck : start")
            step04gcmCheck()
            toast("step04gcmCheck : end")
            Log.e(3)
        }
        val deferredBanner = async {
            toast("step06assetBannerCheck : start")
            step06assetBannerCheck()
            toast("step06assetBannerCheck : end")
            Log.e(4)
        }
        Log.e(5)
        toast("step07v3UpdateCheck : start")
        step07v3UpdateCheck()
        toast("step07v3UpdateCheck : end")
        Log.e(6)
        toast("step11noticeCheck : start")
        step11noticeCheck()
        toast("step11noticeCheck : end")
        Log.e(7)
        toast("step12checkSSO : start")
        step12checkSSO()
        toast("step12checkSSO : end")
        Log.e(8)
        toast("await : start")
        deferredGcm.await()
        deferredBanner.await()
        toast("await : end")
        Log.e(99)
    }

    private suspend fun step00RootingCheck() = suspendCancellableCoroutine<Unit> { continuation ->
        if (IS_ROOTING) {// 루팅 또는 기타오류
            val message = "루팅된 단말입니다. 개인정보 유출의 위험성이 있으므로 " + getAppName(this@IntroAfter) + "를 종료합니다."
            AlertDialog.Builder(this).setMessage(message)
                .setPositiveButton("종료", null)
                .setCancelable(false)
                .setOnDismissListener {
                    continuation.cancel()
                    exit()
                }
                .show()
            return@suspendCancellableCoroutine
        }
        continuation.resume(Unit)
    }

    private suspend fun step01v3Check() = suspendCancellableCoroutine<Unit> { continuation ->
        executeV3Module(object : V3MobilePlusResultListener {
            override fun OnInstallCompleted() {
                continuation.resume(Unit)
            }

            override fun OnPatchState(arg0: Boolean) {}
            override fun OnV3MobilePlusStarted() {}
        })
    }

    private fun step04gcmCheck() {
        gcmRegister(applicationContext, "GCM_SENDER_ID")
    }

    private fun step06assetBannerCheck() {
        //do banner work
    }

    private suspend fun step07v3UpdateCheck() = suspendCancellableCoroutine<Unit> { continuation ->
        if (NOT_INSTALLED_V3) {
            AlertDialog.Builder(this).setMessage("V3 설치안됨")
                .setPositiveButton("설치하기") { _, _ ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("market://details?id=com.ahnlab.v3mobileplus")
                    startActivity(intent)
                    continuation.cancel()
                }
                .setNegativeButton("종료") { _, _ ->
                    continuation.cancel()
                }
                .show()
            return@suspendCancellableCoroutine
        }
        continuation.resume(Unit)
    }

    private suspend fun step11noticeCheck() = suspendCancellableCoroutine<String> { continuation ->
        if (HAS_EVENT) {
            continuation.resume("")
            return@suspendCancellableCoroutine
        }
        AlertDialog.Builder(this)
            .setMessage("신규 고객 이벤트")
            .setPositiveButton("참여하기") { _, _ ->
                continuation.resume("https://event.bank.com")
            }
            .setNegativeButton("나중에보기", null)
            .setOnDismissListener {
                if (!continuation.isCompleted)
                    continuation.resume("")
            }
            .show()
    }


    private fun step12checkSSO() {
        main()
    }

    //------------------------------------------------------------------------------------------
    private fun executeV3Module(v3MobilePlusResultListener: V3MobilePlusResultListener?) {
        v3MobilePlusResultListener?.OnInstallCompleted()
    }

    private fun gcmRegister(applicationContext: Context?, gcM_SENDER_ID: Any?) {

    }

    private fun isV3Updated(): Boolean {
        return false
    }

    private fun exit() {
//        finish()
        toast("종료됨")
        Log.e("종료됨")
    }

    private fun main() {
//        finish()
    }

    interface V3MobilePlusResultListener {
        fun OnInstallCompleted()
        fun OnPatchState(arg0: Boolean)
        fun OnV3MobilePlusStarted()
    }
}

