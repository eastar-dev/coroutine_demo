package com.kebhana.hanapush.transfer

import android.content.Intent
import android.log.Log
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

class FirebaseBefore() : AppCompatActivity() {
    lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(Button(this).apply {
            text = "FirebaseDynamicLinks"
        }.also { button ->
            this.button = button
        })
        dynamicLinks()
    }

    fun dynamicLinks() {
        Log.e()
        var url = "https://droidknights.github.io/2019/"
        val url_image = "https://droidknights.github.io/2019/static/media/2019_dk_title.cf69c879.png"
        FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setDomainUriPrefix("https://coroutine.page.link")
                .setLink(Uri.parse(url))
                .setSocialMetaTagParameters(
                        DynamicLink.SocialMetaTagParameters.Builder().apply {
                            setTitle("드로이드 나이츠 2019")
                            setDescription("드로이드 나이츠 2019 초대하기")
                            setImageUrl(Uri.parse(url_image))//setImageUrl	이 링크와 관련된 이미지의 URL입니다. 이미지는 300x200픽셀 이상, 300KB 미만이어야 합니다.
                        }.build())
                .apply {
                    val dynamicLinkUri = buildDynamicLink().uri
                    Log.e(dynamicLinkUri)
                    val sharingIntent = Intent(Intent.ACTION_SEND).apply {
                        putExtra(Intent.EXTRA_TEXT, dynamicLinkUri.toString())
                        type = "text/plain"
                    }
                    Log.e(sharingIntent)
                    button.setOnClickListener { startActivity(Intent.createChooser(sharingIntent, "드로이드 나이츠 2019")) }
                }
                .apply {
                    buildShortDynamicLink().apply {
                        addOnCompleteListener { task ->
                            //        Log.w(task.isSuccessful(), task.getException())
                            task.exception?.printStackTrace()
                            if (!task.isSuccessful) return@addOnCompleteListener

                            url = task.result?.shortLink.toString()
                            Log.i(task.result?.shortLink)
                            val sharingIntent = Intent(Intent.ACTION_SEND).apply {
                                action = Intent.ACTION_SEND
                                putExtra(Intent.EXTRA_TEXT, url)
                                type = "text/plain"
                            }

                            button.setOnClickListener { startActivity(Intent.createChooser(sharingIntent, "드로이드 나이츠 2019")) }
                        }
                    }
                }

    }
}
