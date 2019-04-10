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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class DownloadWorkBefore : AppCompatActivity() {

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

        setDownload(this)
    }

    inner class BWebViewClient : WebViewClient() {
        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            view?.loadUrl(request?.url?.toString())
            return false
        }
    }

    private fun setDownload(context: Context) {
        webview.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            PermissionRequest.builder(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setDenyMessage("파일 저장 권한을 주셔야만 작업이 가능합니다.")
                .setOnPermissionGrantedListener {
                    val filename = URLUtil.guessFileName(url, contentDisposition, mimetype)
                    DownloadManager.Request(Uri.parse(url)).apply {
                        setMimeType(mimetype)
                        setDescription("Download file...")
                        setTitle(filename)
                        allowScanningByMediaScanner()
                        setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //Notify client once download is completed!
                        setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
                    }.also { request ->
                        val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as?  DownloadManager
                        dm?.enqueue(request)
                        Toast.makeText(context, filename, Toast.LENGTH_LONG).show()
                    }
                }
                .run()
        }
    }

    private fun setDownload2(context: Context) {
        webview.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            AlertDialog.Builder(context)
                .setMessage("다운로드 권한이 없습니다.")
                .setPositiveButton("다운로드") { _, _ ->
                    PermissionRequest.builder(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .setOnPermissionGrantedListener {
                            val filename = URLUtil.guessFileName(url, contentDisposition, mimetype)
                            DownloadManager.Request(Uri.parse(url)).apply {
                                setMimeType(mimetype)
                                setDescription("Download file...")
                                setTitle(filename)
                                allowScanningByMediaScanner()
                                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //Notify client once download is completed!
                                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, filename)
                            }.also { request ->
                                val dm = context.getSystemService(Context.DOWNLOAD_SERVICE) as?  DownloadManager
                                dm?.enqueue(request)
                                Toast.makeText(context, filename, Toast.LENGTH_LONG).show()
                            }
                        }
                        .run()
                }.show()
        }
    }

}

