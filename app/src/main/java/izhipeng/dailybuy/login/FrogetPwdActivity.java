package izhipeng.dailybuy.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.net.URLEncoder;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.ActivityTaskManager;
import izhipeng.dailybuy.library.SecurityUtil;
import izhipeng.dailybuy.library.StringUtil;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class FrogetPwdActivity extends BaseActivity implements View.OnClickListener {

    private Button btn, account_register_get_code_btn;
    private EditText login_username_et, login_pw_et;

    private final static int mMsgCodeRestBtn = 1;
    private final static int mMsgCodeCounting = 2;
    private static int mResendTime = 60;
    private final static int mMaxTime = 60;

    private LinearLayout mBack;
    private TextView mTitle;
    MyInnerHandler mHander = new MyInnerHandler(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_froget);

        ActivityTaskManager.getInstance().putActivity("FrogetPwdActivity", this);
        mResendTime = mMaxTime;
        login_username_et = (EditText) findViewById(R.id.login_username_et);
        login_pw_et = (EditText) findViewById(R.id.login_pw_et);
        login_username_et.setInputType(InputType.TYPE_CLASS_NUMBER);
        login_pw_et.setInputType(InputType.TYPE_CLASS_NUMBER);
        account_register_get_code_btn = (Button) findViewById(R.id.account_register_get_code_btn);
        account_register_get_code_btn.setOnClickListener(this);

        mBack = (LinearLayout) findViewById(R.id.ll_section_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitle = (TextView) findViewById(R.id.tv_section_title_title);
        mTitle.setText("忘记密码");
        btn = (Button) findViewById(R.id.next_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (StringUtil.isEmpty(login_username_et.getText().toString())
                        || StringUtil.isEmpty(login_pw_et.getText().toString())) {

                    showToast("您输入有错误，请检查", 1000);
                    return;
                } else {
                    Intent intent = new Intent();
                    intent.setClass(FrogetPwdActivity.this, NewPwdActivity.class);
                    intent.putExtra("phoneNum", login_username_et.getText().toString());
                    intent.putExtra("code", login_pw_et.getText().toString());
                    startActivity(intent);
                }
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.account_register_get_code_btn:

                if (StringUtil.isEmpty(login_username_et.getText().toString()) ||
                        !StringUtil.isMobileNO(login_username_et.getText().toString())) {

                    showToast("您输入的手机号有误，请再次输入!!", 1000);
                } else {

                    startProgressBar("发送中...", new Thread(), true);
                    getCode();
                }
                break;
            default:
                break;
        }
    }

    private void getCode() {

        mHander.sendEmptyMessage(mMsgCodeCounting);
        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "sendAuthCode.jspa")
                .addParams("mobile", SecurityUtil.encrypt(login_username_et.getText().toString()))
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
                            closeProgressBar();
                            if (jsonObject.getInt("ret") == 1) {

                                showToast(jsonObject.getString("info"), 1000);
                            } else {

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
        WeakReference<FrogetPwdActivity> mFragReference;

        MyInnerHandler(FrogetPwdActivity aFragment) {
            mFragReference = new WeakReference<>(aFragment);
        }

        @Override
        public void handleMessage(Message msg) {
            FrogetPwdActivity theFrag = mFragReference.get();

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
