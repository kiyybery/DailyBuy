package izhipeng.dailybuy.myprefire;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.library.UIHelper;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class SettingActivity extends BaseActivity {

    private LinearLayout mBack;
    private TextView mTitle;
    private RelativeLayout mNotification_layout, mSafe_layout, mClear_layout,
            mContact_layout, mAbout_layout, mLogout_layout;
    private Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mBack = (LinearLayout) findViewById(R.id.ll_section_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitle = (TextView) findViewById(R.id.tv_section_title_title);
        mTitle.setText("设置");

        mNotification_layout = (RelativeLayout) findViewById(R.id.message_layout);
        mNotification_layout.setOnClickListener(this);
        mSafe_layout = (RelativeLayout) findViewById(R.id.safe_layout);
        mSafe_layout.setOnClickListener(this);
        mClear_layout = (RelativeLayout) findViewById(R.id.clear_layout);
        mClear_layout.setOnClickListener(this);
        mContact_layout = (RelativeLayout) findViewById(R.id.contact_layout);
        mContact_layout.setOnClickListener(this);
        mAbout_layout = (RelativeLayout) findViewById(R.id.about_layout);
        mAbout_layout.setOnClickListener(this);
        mLogout_layout = (RelativeLayout) findViewById(R.id.logout_layout);
        mLogout_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.message_layout:
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this, NotifyCtrlActivity.class);
                startActivity(intent);
                break;
            case R.id.safe_layout:

                intent = new Intent();
                intent.setClass(SettingActivity.this, PrivacyActivity.class);
                startActivity(intent);
                break;
            case R.id.contact_layout:

                dialog = UIHelper.buildConfirm(SettingActivity.this, "确认联系客服？",
                        "确认", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();

                                finish();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                            }
                        });
                break;
            case R.id.about_layout:

                intent = new Intent();
                intent.setClass(SettingActivity.this, AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.logout_layout:

                PreferencesUtil.put(DailyBuyApplication.KEY_AUTH, "");
                //PreferencesUtil.put(DailyBuyApplication.KEY_NAME, "");
                //PreferencesUtil.put(DailyBuyApplication.KEY_SEX, 0);
                //PreferencesUtil.put(DailyBuyApplication.KEY_CITY, "");
                //PreferencesUtil.put(DailyBuyApplication.KEY_PATH, "");
                intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }

    }
}
