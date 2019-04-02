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

class IntroBefore : AppCompatActivity() {
    private var rootingChecked: Boolean = false
    private var v3Checked: Boolean = false
    private var gcmChecked: Boolean = false
    private var bannerChecked: Boolean = false
    private var v3UpdateChecked: Boolean = false
    private var noticeChecked: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Button(this).apply {
            setOnClickListener { load() }
            text = "start intro before"
        })
    }

    fun load() {
        Log.e("STEP", rootingChecked, v3Checked, gcmChecked, v3UpdateChecked, noticeChecked)

        if (!rootingChecked) {
            toast("check rootingChecked : $rootingChecked")
            step00RootingCheck()
            return
        }
        if (!v3Checked) {
            toast("check v3Checked : $v3Checked")
            step01v3Check()
            return
        }

        if (!gcmChecked) {
            toast("check gcmChecked : $gcmChecked")
            step04gcmCheck()
            return
        }

        if (!v3UpdateChecked) {
            toast("check v3UpdateChecked : $v3UpdateChecked")
            step07v3UpdateCheck()
            return
        }

        if (!noticeChecked) {
            toast("check noticeChecked : $noticeChecked")
            step11noticeCheck()
            return
        }

        step12checkSSO()
    }

    private fun step00RootingCheck() {
        if (NOT_ROOTING != isRooting()) {// 루팅 또는 기타오류
            val message = "루팅된 단말입니다. 개인정보 유출의 위험성이 있으므로 " + getAppName(this) + "를 종료합니다."
            showDialogForceExit(message)
            return
        }

        rootingChecked = true
        load()
    }

    private fun step01v3Check() {
        executeV3Module(object : V3MobilePlusResultListener {
            override fun OnInstallCompleted() {
                v3Checked = true
                load()
            }

            override fun OnPatchState(arg0: Boolean) {}
            override fun OnV3MobilePlusStarted() {}
        })
    }

    private fun step04gcmCheck() {
        gcmRegister(applicationContext, GCM_SENDER_ID)
        gcmChecked = true
        load()
    }

    private fun step06assetBannerCheck() {
        bannerChecked = true
        load()
    }

    private fun step07v3UpdateCheck() {
        //doV3Update
        AlertDialog.Builder(this).setMessage("V3 설치안됨")
                .setPositiveButton("설치하기") { _, _ ->
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("market://details?id=com.ahnlab.v3mobileplus")
                    startActivity(intent)
                    exit()
                }
                .setNegativeButton("계속진행") { _, _ ->
                    v3UpdateChecked = true
                    load()
                }
                .show()
    }

    private fun step11noticeCheck() {
        noticeChecked = true
        load()
    }

    private fun step12checkSSO() {
        main()
    }

    //------------------------------------------------------------------------------------------
    private fun executeV3Module(v3MobilePlusResultListener: V3MobilePlusResultListener?) {
        v3MobilePlusResultListener?.OnInstallCompleted()
    }

    companion object {
        val NOT_ROOTING: Int = 0
        val GCM_SENDER_ID: Any? = null
    }

    private fun isRooting(): Int {
        return NOT_ROOTING
    }

    private fun showDialogForceExit(message: String) {
        finish()
    }

    private fun gcmRegister(applicationContext: Context?, gcM_SENDER_ID: Any?) {

    }

    private fun isV3Updated(): Boolean {
        return false
    }

    private fun exit() {
        finish()
    }

    private fun main() {
        finish()
    }

    interface V3MobilePlusResultListener {
        fun OnInstallCompleted()
        fun OnPatchState(arg0: Boolean)
        fun OnV3MobilePlusStarted()
    }
}

