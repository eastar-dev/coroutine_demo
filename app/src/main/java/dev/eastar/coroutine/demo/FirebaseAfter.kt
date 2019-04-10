package dev.eastar.coroutine.demo

import android.content.Intent
import android.log.Log
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dev.eastar.coroutine.etc.BaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class FirebaseAfter : BaseActivity() {
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Button(this)
                .apply {
                    text = "Firebase Dynamic Links"
                }.also { button ->
                    this.button = button
                })
        dynamicLinks()
    }

    fun dynamicLinks() = CoroutineScope(Dispatchers.Main).launch {
        try {
            showProgress()
            val builder = getDynamicLinkBuilder()
            setSharing(builder.buildDynamicLink().uri)
            val uri = getShortLink(builder)
            setSharing(uri)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            dismissProgress()
        }
    }

    private fun getDynamicLinkBuilder(): DynamicLink.Builder {
        val url = "https://droidknights.github.io/2019/"
        val url_image = "https://raw.githubusercontent.com/djrain/coroutine_demo/master/release/logobank.png"
        return FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setDomainUriPrefix("https://coroutine.page.link")
                .setLink(Uri.parse(url))
                .setSocialMetaTagParameters(
                        DynamicLink.SocialMetaTagParameters.Builder().apply {
                            setTitle("드로이드 나이츠 2019")
                            setDescription("드로이드 나이츠 2019 초대하기")
                            setImageUrl(Uri.parse(url_image))//setImageUrl	이 링크와 관련된 이미지의 URL입니다. 이미지는 300x200픽셀 이상, 300KB 미만이어야 합니다.
                        }.build())
    }

    private suspend fun getShortLink(builder: DynamicLink.Builder): Uri = suspendCancellableCoroutine { continuation ->
        builder.buildShortDynamicLink().apply {
            addOnSuccessListener { continuation.resume(it.shortLink) }
            addOnCanceledListener { continuation.cancel() }
            addOnFailureListener { continuation.resumeWithException(it) }
            continuation.invokeOnCancellation { Log.w("invokeOnCancellation", it) }
            //for log
            addOnCompleteListener {
                Log.w(it.exception?.printStackTrace())
                if (it.isSuccessful)
                    Log.i(it.result?.shortLink)
                else
                    Log.w(it.isSuccessful)
            }
        }
    }



    var keep = 0L

    fun nano() = (if (keep == 0L) 0L else System.nanoTime() - keep)
            .also {
                keep = System.nanoTime()
            }.comma

    private val Long.comma: String
        get() = String.format(Locale.getDefault(), "%,25d", this)

    private fun setSharing(shortLink: Uri) {
        val sharingIntent = Intent(Intent.ACTION_SEND).apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shortLink.toString())
            type = "text/plain"
        }
        button.setOnClickListener { startActivity(Intent.createChooser(sharingIntent, "드로이드 나이츠 2019")) }
    }

}

