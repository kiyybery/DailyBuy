package izhipeng.dailybuy.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;
import com.umeng.socialize.Config;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.UserInfo;
import izhipeng.dailybuy.library.ActivityTaskManager;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.library.SecurityUtil;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class LoginMainActivity extends BaseActivity implements View.OnClickListener {

    private int userType;
    private LinearLayout mAccount_layout;
    private TextView mDanymic_tv, froget_pwd, right_account;
    private EditText mLogin_username_et, mLogin_pw_et;
    private ImageButton mLogin_show_pwd;
    private Button login_btn;
    private ImageView btn_qq, btn_wx, btn_sina;
    public static IWXAPI mWXapi;
    public Tencent mTencent;
    private SsoHandler mSsoHandler;
    private String access_token, nickname, avatar_url;
    private int loginType;
    private int target;
    private final static int LOGINTYPE_QQ = 0;
    private final static int LOGINTYPE_SINA = 1;
    private final static int LOGINTYPE_WX = 2;

    public final static int TARGET_MYPREFIRE = 4;
    public final static int TARGET_HOME = 1;
    public final static int TARGET_DISCOVER = 2;
    public final static int TARGET_MESSAGE = 3;

    private LinearLayout ll_section_title_back;
    private int type;

    private UMShareAPI umShareAPI = null;
    private String openid, accessToken, userName, userImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //userType = getIntent().getIntExtra("userType", 0);
        setContentView(R.layout.activity_login_main);
        umShareAPI = UMShareAPI.get(this);
        Config.dialogSwitch = true;
        mAccount_layout = (LinearLayout) findViewById(R.id.account_login_layout);
        /*if (userType == 1) {

            mAccount_layout.setVisibility(View.GONE);
            type = 2;
        } else {
            type = 1;
        }*/

        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mLogin_username_et = (EditText) findViewById(R.id.login_username_et);
        mLogin_username_et.setInputType(InputType.TYPE_CLASS_NUMBER);
        mLogin_pw_et = (EditText) findViewById(R.id.login_pw_et);
        mLogin_show_pwd = (ImageButton) findViewById(R.id.login_show_pwd);
        mLogin_show_pwd.setOnClickListener(this);

        login_btn = (Button) findViewById(R.id.login_login_btn);
        login_btn.setOnClickListener(this);

        mDanymic_tv = (TextView) findViewById(R.id.danymic_login);
        mDanymic_tv.setOnClickListener(this);
        froget_pwd = (TextView) findViewById(R.id.froget_pwd);
        froget_pwd.setOnClickListener(this);
        right_account = (TextView) findViewById(R.id.tv_section_title_right);
        right_account.setOnClickListener(this);

        /*btn_qq = (ImageView) findViewById(R.id.login_qq);
        btn_qq.setOnClickListener(this);
        btn_wx = (ImageView) findViewById(R.id.login_wechat);
        btn_wx.setOnClickListener(this);
        btn_sina = (ImageView) findViewById(R.id.login_sina);
        btn_sina.setOnClickListener(this);*/
    }

    public void onAuthClick(View view) {

        SHARE_MEDIA platform = null;
        if (view.getId() == R.id.login_sina) {

            platform = SHARE_MEDIA.SINA;
        } else if (view.getId() == R.id.login_wechat) {

            platform = SHARE_MEDIA.WEIXIN;
        } else if (view.getId() == R.id.login_qq) {

            platform = SHARE_MEDIA.QQ;
        }

        umShareAPI.doOauthVerify(LoginMainActivity.this, platform, umAuthListener);
        startProgressBar("加载中...", new Thread(), false);
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

            showToast(map + "", 1000);
            openid = map.get("openid");
            accessToken = map.get("accessToken");
            umShareAPI.getPlatformInfo(LoginMainActivity.this, share_media, authListener);

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            showToast(throwable + "", 1000);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    private UMAuthListener authListener = new UMAuthListener() {
        int type;

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            if (share_media == SHARE_MEDIA.QQ) {

                type = 1;
            } else if (share_media == SHARE_MEDIA.WEIXIN) {

                type = 2;
            } else if (share_media == SHARE_MEDIA.SINA) {

                type = 3;
            }
            showToast(type + "", 1000);

            OkHttpUtils
                    .post()
                    .url(DailyBuyApplication.IP_URL + "getThirdLogin.jspa")
                    .addParams("type", type + "")
                    .addParams("profile_image_url", map.get("profile_image_url"))
                    .addParams("city", map.get("city"))
                    .addParams("gender", map.get("gender") + "")
                    .addParams("screenname", map.get("screen_name"))
                    .addParams("openid", openid)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {
                            closeProgressBar();
                            showToast(e + "", 1000);
                        }

                        @Override
                        public void onResponse(String s, int i) {

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getInt("ret") == 1) {

                                    showToast(jsonObject.getString("info"), 1000);
                                    closeProgressBar();
                                    Log.i("userId", jsonObject.getString("userId"));

                                    Message message = new Message();
                                    message.what = 1;
                                    handler.sendMessage(message);

                                    PreferencesUtil.put(DailyBuyApplication.KEY_AUTH, jsonObject.getString("userId"));
                                    //PreferencesUtil.put(DailyBuyApplication.KEY_NAME, jsonObject.getString("nickName"));
                                    //PreferencesUtil.put(DailyBuyApplication.KEY_URL, jsonObject.getString("portraitUrl"));
                                    //PreferencesUtil.put(DailyBuyApplication.KEY_CITY, jsonObject.getString("city"));
                                } else {
                                    closeProgressBar();
                                    showToast(jsonObject.getString("info"), 1000);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            finish();
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            showToast(throwable + "", 1000);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
            showToast("cancel" + "", 1000);
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.danymic_login:

                Intent intent = new Intent();
                intent.putExtra("userType", userType);
                intent.setClass(LoginMainActivity.this, DanymicLoginActivity.class);
                startActivity(intent);
                break;
            case R.id.froget_pwd:

                intent = new Intent();
                intent.setClass(LoginMainActivity.this, FrogetPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_section_title_right:

                intent = new Intent();
                intent.putExtra("userType", userType);
                intent.setClass(LoginMainActivity.this, RigstActivity.class);
                startActivity(intent);
                break;

            case R.id.login_show_pwd:

                //控制密码的隐藏与显示
                if (mLogin_pw_et.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    mLogin_pw_et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    mLogin_show_pwd.setBackgroundResource(R.drawable.dlzc_ks_icon);
                } else {
                    mLogin_pw_et.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    mLogin_show_pwd.setBackgroundResource(R.drawable.dlzc_bks_icon);
                }
                //变更属性后记得让光标显示在输入框最后
                Editable editable = mLogin_pw_et.getText();
                Selection.setSelection(editable, editable.length());
                break;

            case R.id.login_login_btn:

                /*PreferencesUtil.put(DailyBuyApplication.KEY_AUTH, "13161851166");
                PreferencesUtil.put(DailyBuyApplication.KEY_PASSWORD, "123456");*/
                startProgressBar("登录中...", new Thread(), true);
                login();
                break;
/*
            case R.id.login_qq:

                break;

            case R.id.login_wechat:

                break;
            case R.id.login_sina:

                break;*/
            default:
                break;
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:

                    Intent intent = new Intent();
                    intent.putExtra("userName", userName);
                    intent.putExtra("userImg", userImg);
                    setResult(RESULT_OK, intent);
                    finish();
                    //ActivityTaskManager.getInstance().removeActivity("SelectActivity");

                    break;
                default:
                    break;
            }
        }
    };

    private void login() {

        Log.i("u_phone", SecurityUtil.encrypt(mLogin_username_et.getText().toString()) + "   " + SecurityUtil.encrypt(mLogin_pw_et.getText().toString()));
        //startProgressBar("登录中...", new Thread(), false);
        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "login.jspa")
                .addParams("mobile", SecurityUtil.encrypt(mLogin_username_et.getText().toString()))
                .addParams("password", SecurityUtil.encrypt(mLogin_pw_et.getText().toString()))
                        //.addParams("userType", type + "")
                /*.addParams("mobile", "nPKBRkal4ffGGElUiNiuzg==")
                .addParams("password", "YOOPXyzb3OI=")*/
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {

                        closeProgressBar();
                        showToast(e + "", 1000);
                    }

                    @Override
                    public void onResponse(String s, int i) {

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int ret = jsonObject.getInt("ret");
                            if (ret == 2) {
                                closeProgressBar();
                                showToast(jsonObject.getString("info"), 1000);
                            } else {
                                closeProgressBar();
                                UserInfo info = new UserInfo();
                                info.setUserId(jsonObject.getString("userId"));
                                info.setNickName(jsonObject.getString("nickName"));
                                info.setUserType(jsonObject.getInt("userType"));
                                info.setCity(jsonObject.getString("city"));
                                info.setSex(jsonObject.getInt("sex"));
                                info.setPortraitUrl(jsonObject.getString("portraitUrl"));

                                userName = jsonObject.getString("nickName");
                                userImg = jsonObject.getString("portraitUrl");

                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);

                                PreferencesUtil.put(DailyBuyApplication.KEY_AUTH, jsonObject.getString("userId"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_TYPE, jsonObject.getInt("userType"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_URL, jsonObject.getString("portraitUrl"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_NAME, jsonObject.getString("nickName"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_SEX, jsonObject.getInt("sex"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_CITY, jsonObject.getString("city"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_CREDITS, jsonObject.getInt("credits"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_AMOUNT, jsonObject.getInt("amount"));


                                //PreferencesUtil.put(DailyBuyApplication.KEY_NAME, jsonObject.getString("nickName"));
                                //PreferencesUtil.put(DailyBuyApplication.KEY_CREDITS, jsonObject.getString("credits"));
                                //PreferencesUtil.put(DailyBuyApplication);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    //更新未umeng login，暂时废弃。
    /*private void sinalogin() {

        loginType = LOGINTYPE_SINA;
        startProgressBar("登录中...", new Thread(), true);
        mWeiboAuth = new WeiboAuth(LoginMainActivity.this, DailyBuyApplication.SINA_APP_KEY,
                DailyBuyApplication.SINA_REDIRECT_URL, DailyBuyApplication.SINA_SCOPE);
        mSsoHandler = new SsoHandler(LoginMainActivity.this, mWeiboAuth);
        mSsoHandler.authorize(new AuthListener());
    }

    private void wxlogin() {

        if (!DailyBuyApplication.api.isWXAppInstalled()) {
            Toast.makeText(this, "您还未安装微信客户端", Toast.LENGTH_LONG).show();
            return;
        }

        loginType = LOGINTYPE_WX;
        startProgressBar("登录中...", new Thread(), true);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "baihui_wx_login";
        boolean isWX = DailyBuyApplication.api.sendReq(req);
        Log.e("test", "isWx" + isWX);
        finish();
        ActivityTaskManager.getInstance().removeActivity("SelectActivity");
    }

    private void qqlogin() {

        if (mQQAuth.isSessionValid()) {
            mQQAuth.logout(this);
        }
        loginType = LOGINTYPE_QQ;
        startProgressBar("登录中...", new Thread(), true);
        LoginListener listener = new LoginListener(this);
        mTencent.login(this, "all", listener);
    }

    class AuthListener implements WeiboAuthListener {

        @Override
        public void onCancel() {
            closeProgressBar();
            showToast("用户取消登录", Toast.LENGTH_SHORT);
        }

        @Override
        public void onComplete(Bundle values) {

            Oauth2AccessToken accessToken = Oauth2AccessToken
                    .parseAccessToken(values);
            updateSinaUserInfo(accessToken);
        }

        @Override
        public void onWeiboException(WeiboException e) {
            closeProgressBar();
            showToast("登录异常", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }

    }

    private void updateSinaUserInfo(Oauth2AccessToken accessToken) {
        UsersAPI mUsersAPI = new UsersAPI(accessToken);
        long uid = Long.parseLong(accessToken.getUid());
        openid = String.valueOf(uid);
        RequestListener mlistener = new RequestListener() {

            @Override
            public void onWeiboException(WeiboException e) {
                closeProgressBar();
                showToast("获取用户信息异常", Toast.LENGTH_SHORT);
                e.printStackTrace();

            }

            @Override
            public void onComplete(String response) {
                if (!StringUtil.isEmpty(response)) {
                    User user = User.parse(response);
                    nickname = user.name;
                    avatar_url = user.avatar_large;
                    //doLogin();
                    closeProgressBar();
                }
            }
        };
        mUsersAPI.show(uid, mlistener);
    }

    private class LoginListener extends BaseUiListener {

        public LoginListener(BaseActivity context) {
            super(context);

        }

        @Override
        protected void doComplete(JSONObject values) {
            try {
                openid = values.getString("openid");
                Log.e("openid:", openid);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                access_token = values.getString("access_token");
                Log.e("access_token:", access_token);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            updateQQUserInfo();
            //closeProgressBar();
        }

        @Override
        public void onCancel() {
            closeProgressBar();
            showToast("用户取消登录", Toast.LENGTH_SHORT);
        }
    }

    private void updateQQUserInfo() {
        *//*if (mQQAuth == null || !mQQAuth.isSessionValid())
            return;*//*
        BaseUiListener listener = new BaseUiListener(this) {

            @Override
            public void doComplete(JSONObject response) {

                try {
                    nickname = response.getString("nickname");
                    Log.e("nickname", nickname);
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (response.has("figureurl_qq_2")) {
                    try {
                        avatar_url = response.getString("figureurl_qq_2");
                        Log.e("avatar_url", avatar_url);
                    } catch (JSONException e) {
                    }
                } else if (response.has("figureurl_qq_1")) {
                    try {
                        avatar_url = response.getString("figureurl_qq_1");
                        Log.e("avatar_url", avatar_url);
                    } catch (JSONException e) {
                    }
                }

                //Log.e("uinfo", nickname + "  url:" + avatar_url);
                //doLogin();
                closeProgressBar();
            }
        };
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /*switch (loginType) {

            case LOGINTYPE_QQ:
                mTencent.onActivityResult(requestCode, resultCode, data);
                //closeProgressBar();
                break;
            case LOGINTYPE_SINA:
                if (mSsoHandler != null) {
                    mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
                }
                break;
        }*/
        umShareAPI.onActivityResult(requestCode, resultCode, data);
    }
}
