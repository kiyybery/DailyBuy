package izhipeng.dailybuy.login;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuth;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.MainActivity;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.UserInfo;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.library.SecurityUtil;
import izhipeng.dailybuy.library.StringUtil;
import izhipeng.dailybuy.myprefire.MyPreFireFragment;
import izhipeng.dailybuy.tencent.BaseUiListener;
import izhipeng.dailybuy.weibo.User;
import izhipeng.dailybuy.weibo.UsersAPI;
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
    public static QQAuth mQQAuth;
    public static IWXAPI mWXapi;
    public Tencent mTencent;
    private WeiboAuth mWeiboAuth;
    private SsoHandler mSsoHandler;
    private String openid, access_token, nickname, avatar_url;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userType = getIntent().getIntExtra("userType", 0);
        setContentView(R.layout.activity_login_main);
        mAccount_layout = (LinearLayout) findViewById(R.id.account_login_layout);
        if (userType == 1) {

            mAccount_layout.setVisibility(View.GONE);
        }
        mQQAuth = QQAuth.createInstance(DailyBuyApplication.TENCENT_APP_ID,
                getApplicationContext());
        mTencent = Tencent.createInstance(DailyBuyApplication.TENCENT_APP_ID,
                getApplicationContext());

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

        btn_qq = (ImageView) findViewById(R.id.login_qq);
        btn_qq.setOnClickListener(this);
        btn_wx = (ImageView) findViewById(R.id.login_wechat);
        btn_wx.setOnClickListener(this);
        btn_sina = (ImageView) findViewById(R.id.login_sina);
        btn_sina.setOnClickListener(this);
    }

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

            case R.id.login_qq:

                qqlogin();
                break;

            case R.id.login_wechat:

                wxlogin();
                break;
            case R.id.login_sina:

                sinalogin();
                break;
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
                    setResult(RESULT_OK, intent);
                    finish();

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

                                Message message = new Message();
                                message.what = 1;
                                handler.sendMessage(message);

                                PreferencesUtil.put(DailyBuyApplication.KEY_AUTH, jsonObject.getString("userId"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_TYPE, jsonObject.getInt("userType"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_URL, jsonObject.getString("portraitUrl"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_NAME, jsonObject.getString("nickName"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_SEX, jsonObject.getInt("sex"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_CITY, jsonObject.getString("city"));

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

    private void sinalogin() {

        loginType = LOGINTYPE_SINA;
        startProgressBar("登录中...", new Thread(), true);
        mWeiboAuth = new WeiboAuth(LoginMainActivity.this, DailyBuyApplication.SINA_APP_KEY,
                DailyBuyApplication.SINA_REDIRECT_URL, DailyBuyApplication.SINA_SCOPE);
        mSsoHandler = new SsoHandler(LoginMainActivity.this, mWeiboAuth);
        mSsoHandler.authorize(new AuthListener());
    }

    private void wxlogin() {

        if (mWXapi == null) {

            mWXapi = WXAPIFactory.createWXAPI(getApplicationContext(),
                    DailyBuyApplication.WX_APP_ID, false);
        }

        if (!mWXapi.isWXAppInstalled()) {

            return;
        }

        mWXapi.registerApp(DailyBuyApplication.WX_APP_ID);
        loginType = LOGINTYPE_WX;
        startProgressBar("登录中...", new Thread(), true);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        //req.state = "wechat_sdk_login";
        mWXapi.sendReq(req);
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
        }

        @Override
        public void onCancel() {
            closeProgressBar();
            showToast("用户取消登录", Toast.LENGTH_SHORT);
        }
    }

    private void updateQQUserInfo() {
        if (mQQAuth == null || !mQQAuth.isSessionValid())
            return;
        BaseUiListener listener = new BaseUiListener(this) {

            @Override
            public void doComplete(JSONObject response) {

                try {
                    nickname = response.getString("nickname");
                } catch (Exception e) {
                    // TODO: handle exception
                }
                if (response.has("figureurl_qq_2")) {
                    try {
                        avatar_url = response.getString("figureurl_qq_2");
                    } catch (JSONException e) {
                    }
                } else if (response.has("figureurl_qq_1")) {
                    try {
                        avatar_url = response.getString("figureurl_qq_1");
                    } catch (JSONException e) {
                    }
                }
                //doLogin();
                closeProgressBar();
            }
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (loginType) {

            case LOGINTYPE_QQ:
                mTencent.onActivityResult(requestCode, resultCode, data);
                break;
            case LOGINTYPE_SINA:
                if (mSsoHandler != null) {
                    mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
                }
                break;
        }
    }
}