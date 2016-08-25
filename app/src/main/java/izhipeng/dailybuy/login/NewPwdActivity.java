package izhipeng.dailybuy.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sina.weibo.sdk.api.share.Base;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.ActivityTaskManager;
import izhipeng.dailybuy.library.SecurityUtil;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class NewPwdActivity extends BaseActivity implements View.OnClickListener {

    private LinearLayout mBack;
    private TextView mTitle;
    private EditText mLogin_pw_et;
    private ImageButton mLogin_show_pwd;
    private Button btn;
    private String phoneNum, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newpwd);

        mBack = (LinearLayout) findViewById(R.id.ll_section_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTitle = (TextView) findViewById(R.id.tv_section_title_title);
        mTitle.setText("新密码");

        mLogin_pw_et = (EditText) findViewById(R.id.login_pw_et);
        mLogin_show_pwd = (ImageButton) findViewById(R.id.login_show_pwd);
        mLogin_show_pwd.setOnClickListener(this);
        btn = (Button) findViewById(R.id.login_login_btn);
        btn.setOnClickListener(this);
        phoneNum = getIntent().getStringExtra("phoneNum");
        code = getIntent().getStringExtra("code");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

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

                startProgressBar("请等待...", new Thread(), true);
                updatePwd();
                break;
            default:
                break;
        }
    }

    private void updatePwd() {

        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "modifyPwd.jspa")
                .addParams("mobile", phoneNum)
                .addParams("code", code)
                .addParams("newPwd", SecurityUtil.encrypt(mLogin_pw_et.getText().toString()))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        closeProgressBar();
                        showToast(e + "", 1000);
                        /*finish();
                        ActivityTaskManager.getInstance().removeActivity("FrogetPwdActivity");*/
                    }

                    @Override
                    public void onResponse(String s, int i) {

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            int ret = jsonObject.getInt("ret");
                            if (ret == 1) {
                                closeProgressBar();
                                showToast(jsonObject.getString("info"), 1000);
                            } else {
                                closeProgressBar();
                                showToast(jsonObject.getString("info"), 1000);
                                finish();
                                ActivityTaskManager.getInstance().closeAllActivityExceptOne("FrogetPwdActivity");
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
