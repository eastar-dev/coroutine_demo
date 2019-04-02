package dev.eastar.coroutine.demo

import android.content.Intent
import android.log.Log
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import dev.eastar.coroutine.etc.BaseActivity

class FirebaseBefore : BaseActivity() {
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

    fun dynamicLinks() {
        Log.e()
        var url = "https://droidknights.github.io/2019/"
        val url_image = "https://droidknights.github.io/2019/static/media/2019_dk_title.cf69c879.png"
        val builder = FirebaseDynamicLinks.getInstance()
                .createDynamicLink()
                .setDomainUriPrefix("https://coroutine.page.link")
                .setLink(Uri.parse(url))
                .setSocialMetaTagParameters(
                        DynamicLink.SocialMetaTagParameters.Builder().apply {
                            setTitle("드로이드 나이츠 2019")
                            setDescription("드로이드 나이츠 2019 초대하기")
                            setImageUrl(Uri.parse(url_image))//setImageUrl	이 링크와 관련된 이미지의 URL입니다. 이미지는 300x200픽셀 이상, 300KB 미만이어야 합니다.
                        }.build())

        builder.apply {
            val dynamicLinkUri = buildDynamicLink().uri
            Log.e(dynamicLinkUri)
            setSharing(dynamicLinkUri)
        }

        builder.apply {
            buildShortDynamicLink().apply {
                addOnCompleteListener { task ->
                    task.exception?.printStackTrace()
                    if (!task.isSuccessful) return@addOnCompleteListener
                    Log.i(task.result?.shortLink)
                    setSharing(task.result?.shortLink)
                }
            }
        }
    }

    fun setSharing(shortLink: Uri?) {
        val sharingIntent = Intent(Intent.ACTION_SEND).apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shortLink?.toString())
            type = "text/plain"
        }
        button.setOnClickListener { startActivity(Intent.createChooser(sharingIntent, "드로이드 나이츠 2019")) }
    }

}
