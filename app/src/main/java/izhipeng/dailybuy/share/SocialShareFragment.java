package izhipeng.dailybuy.share;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;
import okhttp3.Call;

/**
 * Created by Administrator on 2015/12/11 0011.
 */
public class SocialShareFragment extends DialogFragment implements View.OnClickListener {

    private static final String TAG = SocialShareFragment.class.getSimpleName();
    private Button mCancel_btn, share_btn_wx, share_btn_wxcircle, share_btn_weibo, share_btn_qq;
    //UMSocialService mController;
    Bundle data;
    String shareTitle, shareDesp, shareImageUrl, shareWebUrl, infoId;

    public static SocialShareFragment getInstance() {
        SocialShareFragment f = new SocialShareFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(style, theme);
        //data = getArguments();
        //mController = ((ShareActivity) getActivity()).getUMSocialService();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        getDialog().setCanceledOnTouchOutside(true);

        View view = inflater.inflate(R.layout.social_share_fragment, container, false);

        data = getArguments();
        shareTitle = data.getString("shareTitle");
        shareDesp = data.getString("shareDesp");
        shareImageUrl = data.getString("shareImageUrl");
        shareWebUrl = data.getString("shareWebUrl");
        infoId = data.getString("infoId");

        mCancel_btn = (Button) view.findViewById(R.id.btn_cancel_social_share);
        mCancel_btn.setOnClickListener(this);

        share_btn_wx = (Button) view.findViewById(R.id.share_to_weixin);
        share_btn_wx.setOnClickListener(this);

        share_btn_wxcircle = (Button) view.findViewById(R.id.share_to_wxcircle);
        share_btn_wxcircle.setOnClickListener(this);

        share_btn_weibo = (Button) view.findViewById(R.id.share_to_sina);
        share_btn_weibo.setOnClickListener(this);

        share_btn_qq = (Button) view.findViewById(R.id.share_to_qq);
        share_btn_qq.setOnClickListener(this);

        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onClick(View view) {
        setShareContorller();
        switch (view.getId()) {

            case R.id.btn_cancel_social_share:
                //getActivity().onBackPressed();
                getActivity().finish();
                break;

            case R.id.share_to_weixin:

                /*WeiXinShareContent weixin = new WeiXinShareContent();
                weixin.setShareContent(shareDesp);
                // 设置微信title
                weixin.setTitle(shareTitle);
                weixin.setShareImage(new UMImage(this.getActivity(),
                        shareImageUrl));

                weixin.setTargetUrl(shareWebUrl);
                mController.setShareMedia(weixin);
                doPostShare(SHARE_MEDIA.WEIXIN);*/

                ShareAction shareAction = new ShareAction(getActivity());
                shareAction.withText(shareDesp);
                shareAction.withMedia(new UMImage(this.getActivity(), shareImageUrl));
                shareAction.withTitle(shareTitle);
                shareAction.withTargetUrl(shareWebUrl);
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener).share();

                break;

            case R.id.share_to_wxcircle:

                // 设置微信朋友圈分享内容
                /*CircleShareContent circleMedia = new CircleShareContent();
                circleMedia.setShareContent(shareDesp);
                // 设置朋友圈title
                circleMedia.setTitle(shareTitle);
                circleMedia.setShareImage(new UMImage(this.getActivity(),
                        shareImageUrl));
                circleMedia.setTargetUrl(shareWebUrl);
                mController.setShareMedia(circleMedia);
                doPostShare(SHARE_MEDIA.WEIXIN_CIRCLE);*/

                shareAction = new ShareAction(getActivity());
                shareAction.withText(shareDesp);
                shareAction.withMedia(new UMImage(this.getActivity(), shareImageUrl));
                shareAction.withTitle(shareTitle);
                shareAction.withTargetUrl(shareWebUrl);
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener).share();
                break;

            case R.id.share_to_sina:

                /*SinaShareContent sina = new SinaShareContent();
                // 设置分享内容的标题
                sina.setTitle(shareTitle);
                sina.setShareContent(shareDesp);
                // 设置点击消息的跳转URL
                sina.setTargetUrl(shareWebUrl);
                // 设置分享图片
                sina.setShareImage(new UMImage(getActivity(), shareImageUrl));
                mController.setShareMedia(sina);
                doPostShare(SHARE_MEDIA.SINA);*/
                break;

            case R.id.share_to_qq:

                /*QQShareContent qq = new QQShareContent();
                // 设置分享文字
                qq.setShareContent(shareDesp);
                // 设置点击消息的跳转URL
                qq.setTargetUrl(shareWebUrl);
                // 设置分享内容的标题
                qq.setTitle(shareTitle);
                // 设置分享图片
                qq.setShareImage(new UMImage(getActivity(), shareImageUrl));
                mController.setShareMedia(qq);
                doPostShare(SHARE_MEDIA.QQ);*/

                Log.e("targeturl", shareWebUrl);
                shareAction = new ShareAction(getActivity());
                shareAction.withText(shareDesp);
                shareAction.withMedia(new UMImage(this.getActivity(), shareImageUrl));
                shareAction.withTitle(shareTitle);
                shareAction.withTargetUrl(shareWebUrl);
                shareAction.setPlatform(SHARE_MEDIA.QQ)
                        .setCallback(umShareListener).share();
                break;

            default:

                break;
        }


    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            com.umeng.socialize.utils.Log.d("plat", "platform" + platform);

            OkHttpUtils
                    .post()
                    .url(DailyBuyApplication.IP_URL + "updateFavorableShareAndLike.jspa")
                    .addParams("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))
                    .addParams("infoId", infoId)
                    .addParams("iType", 1 + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {

                        }

                        @Override
                        public void onResponse(String s, int i) {

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getInt("ret") == 1) {

                                    Toast.makeText(getActivity(), jsonObject.getString("info"), Toast.LENGTH_LONG)
                                            .show();
                                } else {

                                    Toast.makeText(getActivity(), jsonObject.getString("info"), Toast.LENGTH_LONG)
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            Log.e("error", t + "");

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this.getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    private void setShareContorller() {

        // weixin and circle
        String appID = "wx3ddafc9c0b621abb";
        String appSecret = "d7913b08c32861510d69b2e344d938c8";
//         添加微信平台
        //UMWXHandler wxHandler = new UMWXHandler(getActivity(), appID, appSecret);
        //wxHandler.addToSocialSDK();
        // 添加微信朋友圈
        //UMWXHandler wxCircleHandler = new UMWXHandler(getActivity(), appID, appSecret);
        //wxCircleHandler.setToCircle(true);
        //wxCircleHandler.addToSocialSDK();

        // 添加新浪 sso授权
        //String sinaAppKey = "2537604217";
        //String sinaAppSecret = "d7febe983f96efda5041ff4284b38cbe";
        //mController.getConfig().setSsoHandler(new SinaSsoHandler());

        //   添加qq
        String qqID = "101349957";
        String qqKey = "3fc37bbd87fd2f898c53522fc26db0f3";
        //参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
        //UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(getActivity(), qqID, qqKey);
        //qqSsoHandler.addToSocialSDK();
    }

   /* private void doPostShare(SHARE_MEDIA shareMediaType) {
        mController.postShare(this.getActivity(), shareMediaType,
                new SocializeListeners.SnsPostListener() {
                    @Override
                    public void onStart() {
                        Toast.makeText(getActivity(), "开始分享.",
                                Toast.LENGTH_SHORT).show();
                        *//*OkHttpUtils
                                .post()
                                .url(DailyBuyApplication.IP_URL + "updateFavorableShareAndLike.jspa")
                                .addParams("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))
                                .addParams("infoId", infoId)
                                .addParams("iType", 1 + "")
                                .build()
                                .execute(new StringCallback() {
                                    @Override
                                    public void onError(Call call, Exception e, int i) {

                                    }

                                    @Override
                                    public void onResponse(String s, int i) {

                                        try {
                                            JSONObject jsonObject = new JSONObject(s);
                                            if (jsonObject.getInt("ret") == 1) {

                                                Toast.makeText(getActivity(), jsonObject.getString("info"), Toast.LENGTH_LONG)
                                                        .show();
                                            } else {

                                                Toast.makeText(getActivity(), jsonObject.getString("info"), Toast.LENGTH_LONG)
                                                        .show();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                });*//*
                    }

                    @Override
                    public void onComplete(SHARE_MEDIA platform, int eCode,
                                           SocializeEntity entity) {
                        Log.i(TAG, "onComplete---eCode=" + eCode);
//                        String showText = platform.toString();
//                        if (eCode == 200) {
//                            showText += "平台分享成功";
//                            // todo 有一个疑问，mainActivity 会否已经destory??
//
//                        } else {
//                            showText += "平台分享失败[" + eCode + "]";
//
//                        }
                    }
                });
    }*/
}
