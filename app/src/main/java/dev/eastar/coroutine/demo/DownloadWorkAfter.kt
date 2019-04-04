package dev.eastar.coroutine.demo

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.permission.PermissionRequest
import android.webkit.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class DownloadWorkAfter : AppCompatActivity() {

    companion object {
        const val URL = "https://github.com/droidknights/DroidKnights-Festival-2019-flutter/releases/tag/1.1.0"
    }

    private lateinit var webview: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(WebView(this).also {
            webview = it
            it.loadUrl(URL)
        })

        webview.apply {
            isVerticalScrollBarEnabled = true
            isHorizontalScrollBarEnabled = false
            isFocusableInTouchMode = true 
            isFocusable = true
        }.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            loadsImagesAutomatically = true
            allowFileAccess = true
            allowFileAccessFromFileURLs = true
            domStorageEnabled = true
            databaseEnabled = true
            setAppCacheEnabled(true)
            cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK
            setSupportMultipleWindows(true)
            useWideViewPort = true
            loadWithOverviewMode = true
        }

        webview.webViewClient = BWebViewClient()

        setDownload()
    }

    inner class BWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            view?.loadUrl(request?.url?.toString())
            return false
        }
    }

    private fun setDownload() {
        webview.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            CoroutineScope(Dispatchers.Main).launch {
                checkPermission()
                downloadStart(url, userAgent, contentDisposition, mimetype, contentLength)
            }
        }
    }

    private suspend fun checkPermission() = suspendCancellableCoroutine<Unit> { cancellableContinuation ->
        PermissionRequest.builder(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setOnPermissionGrantedListener { cancellableContinuation.resume(Unit) }
                .setOnPermissionDeniedListener { _, _ -> cancellableContinuation.cancel() }
                .run()
    }

    @Suppress("SpellCheckingInspection", "UNUSED_PARAMETER")
    fun downloadStart(url: String, userAgent: String, contentDisposition: String, mimetype: String, contentLength: Long) {
        val filename = URLUtil.guessFileName(url, contentDisposition, mimetype)

        DownloadManager.Request(Uri.parse(url)).apply {
            setMimeType(mimetype)
            setDescription("Download file...")
            setTitle(filename)
            allowScanningByMediaScanner()
            setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //Notify client once download is completed!
            setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
        }.also { request ->
            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as?  DownloadManager
            dm?.enqueue(request)
            Toast.makeText(this, filename, Toast.LENGTH_LONG).show()
        }
    }
}