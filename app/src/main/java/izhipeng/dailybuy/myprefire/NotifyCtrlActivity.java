package izhipeng.dailybuy.myprefire;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sina.weibo.sdk.api.share.Base;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.SettingPrefernceUtil;
import izhipeng.dailybuy.widget.SwitchView;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class NotifyCtrlActivity extends BaseActivity {

    private LinearLayout mBack;
    private TextView mTitle;

    private SwitchView view_switch1;
    private Boolean isChecked = false;
    private SettingPrefernceUtil mSettingPrefernceUtil;
    private LinearLayout ll_section_title_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);

        mBack = (LinearLayout) findViewById(R.id.ll_section_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitle = (TextView) findViewById(R.id.tv_section_title_title);
        mTitle.setText("消息通知");

        mSettingPrefernceUtil = new SettingPrefernceUtil(this);
        view_switch1 = (SwitchView) findViewById(R.id.view_switch1);
        view_switch1.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                view_switch1.toggleSwitch(true);
                showToast("通知开启", 1000);
                isChecked = true;
                mSettingPrefernceUtil.setSettingMsgNotification(true);
            }

            @Override
            public void toggleToOff() {
                view_switch1.toggleSwitch(false);
                showToast("通知关闭", 1000);
                isChecked = false;
                mSettingPrefernceUtil.setSettingMsgNotification(false);
            }
        });
        view_switch1.toggleSwitch(mSettingPrefernceUtil.getSettingMsgNotification());
    }
}
