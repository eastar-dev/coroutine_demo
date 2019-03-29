package com.kebhana.hanapush.transfer

import android.content.Intent
import android.log.Log
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.dynamiclinks.DynamicLink
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.kebhana.hanapush.transfer.FirebaseBefore.EXTRA.Companion.urlAdr

class FirebaseBefore : AppCompatActivity() {
    interface EXTRA {
        companion object {
            val sendMsg = "sendMsg"
            val urlAdr = "urlAdr"
        }
    }

//    protected fun onCreate(savedInstanceState: Bundle) {
//        super.onCreate(savedInstanceState)
//        setTitle("완료 결과 전달하기 알리기")
//    }

    private var url: String? = null

    protected fun onParseExtra() {
        url = intent.extras.getString(EXTRA.sendMsg)
    }

    protected fun onLoadOnce() {
        FirebaseDynamicLinks.getInstance().createDynamicLink().apply {
            setLink(Uri.parse(url))
            setDomainUriPrefix("String")
            setSocialMetaTagParameters(
                    DynamicLink.SocialMetaTagParameters.Builder().apply {
                        setTitle("드로이드 나이츠 2019")
                        setDescription("드로이드 나이츠 2019 초대하기")
                        setImageUrl(Uri.parse("https://droidknights.github.io/2019/static/media/2019_dk_title.cf69c879.png"))//setImageUrl	이 링크와 관련된 이미지의 URL입니다. 이미지는 300x200픽셀 이상, 300KB 미만이어야 합니다.
                    }.build())
            buildShortDynamicLink().apply {
                addOnCompleteListener { task ->
                    //        Log.w(task.isSuccessful(), task.getException())
                    task.exception?.printStackTrace()
                    if (!task.isSuccessful) return@addOnCompleteListener

                    Log.i(url, task.result?.shortLink)

                    Intent(Intent.ACTION_SEND, task.result?.shortLink)


                    url = task.result?.shortLink.toString()

                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, url)
                        type = "text/plain"
                    }
                    startActivity(sendIntent)

//                    setOnClickListener({ v ->
//                        mMaybeSendMessage = true
//                        val intent_sms = Sms.getIntentSms(mContext, rmteTelNo, String.format(VV.L, "%s\n\n%s", sendMsg, urlAdr))
//                        val intent_main = Intent(mContext, AppMain::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                        startActivities(arrayOf<Intent>(intent_main, intent_sms))
//                    })
//                    bb.kakao.setOnClickListener({ v ->
//                        mMaybeSendMessage = true
//                        main()
//                        KakaoLinkTool.sendMessage(mContext, sendMsg, null, urlAdr)
//                    })
                }
            }
        }

//    .addOnCompleteListener(this,
//    {
//        task ->
//    })
//    bb.sms.setOnClickListener(
//    {
//        v ->
//        mMaybeSendMessage = true
//        val intent_sms = Sms.getIntentSms(mContext, rmteTelNo, String.format(VV.L, "%s\n\n%s", sendMsg, urlAdr))
//        val intent_main = Intent(mContext, AppMain::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
//        startActivities(arrayOf<Intent>(intent_main, intent_sms))
//    })
//    bb.kakao.setOnClickListener(
//    {
//        v ->
//        mMaybeSendMessage = true
//        main()
//        KakaoLinkTool.sendMessage(mContext, sendMsg, null, urlAdr)
//    })
//    bb.open .setOnCheckChangeListener(
//    { btn, checked -> BH.setVisibility(bb.openLayout, checked) })
//
//    val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
//    if (hour >= 22)
//    showDialog("<< 주의사항 >>\n" + "입금정보를 오늘 자정(24시)까지 \n" + "투입해야 자금을 수취할 수 있습니다.", "확인", null)
    }

//    protected fun onBackPressedEx(): Boolean {
//        if (super.onBackPressedEx())
//            return true
//
//        if (!mMaybeSendMessage) {
//            showDialogSticky("선택하신 메신저를 통해 입금계좌 입력 페이지(URL)를 보내셔야 받는 분은 송금 받기가 가능합니다.\n\n이체거래 유효시간은 이체 신청 당일 24시까지만 유효합니다."//
//                    , "취소", null, "메시지 보내지 않고 닫기") { dialog, which ->
//                showDialog(" 메인 > 이체 > 최근내역 > 대기중 ICON 을 선택하셔서 언제든 다시 보낼수 있어요", "확인", null)//
//                        .setOnDismissListener({ d -> main() })
//            }//
//            return true
//        }
//
//        main()
//        return true
//    }
}
