package izhipeng.dailybuy.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.sina.weibo.sdk.api.share.Base;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.UserInfo;
import izhipeng.dailybuy.library.ActivityTaskManager;
import izhipeng.dailybuy.library.SecurityUtil;
import izhipeng.dailybuy.library.StringUtil;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class RigstActivity extends BaseActivity implements View.OnClickListener {

    private int userType;
    private LinearLayout account_login_layout;
    private EditText login_username_et, login_pw_et_code, login_pw_et;
    private Button account_register_get_code_btn, login_login_btn;
    private final static int mMsgCodeRestBtn = 1;
    private final static int mMsgCodeCounting = 2;
    private static int mResendTime = 60;
    private final static int mMaxTime = 60;
    private int rigstType;

    MyInnerHandler mHander = new MyInnerHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rigst);

        ActivityTaskManager.getInstance().putActivity("RigstActivity", this);
        mResendTime = mMaxTime;
        userType = getIntent().getIntExtra("userType", 0);
        account_login_layout = (LinearLayout) findViewById(R.id.account_login_layout);

        if (userType == 1) {

            account_login_layout.setVisibility(View.GONE);
            rigstType = 2;
        } else {

            rigstType = 1;
        }

        login_username_et = (EditText) findViewById(R.id.login_username_et);
        login_username_et.setInputType(InputType.TYPE_CLASS_NUMBER);
        login_pw_et_code = (EditText) findViewById(R.id.login_pw_et_code);
        login_pw_et_code.setInputType(InputType.TYPE_CLASS_NUMBER);
        login_pw_et = (EditText) findViewById(R.id.login_pw_et);

        account_register_get_code_btn = (Button) findViewById(R.id.account_register_get_code_btn);
        account_register_get_code_btn.setOnClickListener(this);
        login_login_btn = (Button) findViewById(R.id.login_login_btn);
        login_login_btn.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.account_register_get_code_btn:
                if (!StringUtil.isMobileNO(login_username_et.getText().toString())) {

                    showToast("请输入正确手机号！", 1000);
                    return;
                } else {
                    startProgressBar("加载中...", new Thread(), true);
                    getCode();
                }
                break;

            case R.id.login_login_btn:

                rigst();
                break;
            default:
                break;
        }
    }

    private void rigst() {

        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "register.jspa")
                .addParams("mobile", SecurityUtil.encrypt(login_username_et.getText().toString()))
                .addParams("password", SecurityUtil.encrypt(login_pw_et.getText().toString()))
                .addParams("code", login_pw_et_code.getText().toString())
                .addParams("userType", rigstType + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        closeProgressBar();
                        showToast(e + "", 1000);
                    }

                    @Override
                    public void onResponse(String s, int i) {

                        closeProgressBar();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("ret") == 2) {

                                showToast(jsonObject.getString("info"), 1000);
                            } else {
                                showToast(jsonObject.getString("info"), 1000);
                                UserInfo info = new UserInfo();
                                info.setUserId(jsonObject.getString("userId"));
                                finish();
                                ActivityTaskManager.getInstance().removeActivity("SelectActivity");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getCode() {

        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "sendAuthCode.jspa")
                .addParams("mobile", SecurityUtil.encrypt(login_username_et.getText().toString()))
                        //.addParams("password", URLEncoder.encode(SecurityUtil.encrypt(login_pw_et.getText().toString())))
                        //.addParams("code", login_pw_et_code.getText().toString())
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

                                closeProgressBar();
                                showToast(jsonObject.getString("info"), 1000);
                            } else {
                                closeProgressBar();
                                showToast(jsonObject.getString("info"), 1000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //开启Handler,
    static class MyInnerHandler extends Handler {
        WeakReference<RigstActivity> mFragReference;

        MyInnerHandler(RigstActivity aFragment) {
            mFragReference = new WeakReference<>(aFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            RigstActivity theFrag = mFragReference.get();

            switch (msg.what) {
                case mMsgCodeRestBtn:
                    mResendTime = mMaxTime;
                    theFrag.account_register_get_code_btn.setClickable(true);
                    theFrag.account_register_get_code_btn.setText("重新发送");
                    break;
                case mMsgCodeCounting:
                    mResendTime--;

                    if (mResendTime > 0) {
                        theFrag.account_register_get_code_btn.setClickable(false);
                        theFrag.account_register_get_code_btn.setText(theFrag.getString(R.string.send_code_counting_text, mResendTime));
                        sendEmptyMessageDelayed(mMsgCodeCounting, 1000);
                    } else {
                        sendEmptyMessageDelayed(mMsgCodeRestBtn, 1000);
                    }
                    break;
            }
        }
    }
}
