package izhipeng.dailybuy.myprefire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.login.FrogetPwdActivity;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class PrivacyActivity extends BaseActivity {

    private LinearLayout mBack;
    private RelativeLayout privacy_layout_modify;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        mBack = (LinearLayout) findViewById(R.id.ll_section_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitle = (TextView) findViewById(R.id.tv_section_title_title);
        mTitle.setText("隐私与安全");

        privacy_layout_modify = (RelativeLayout) findViewById(R.id.privacy_layout_modify);
        privacy_layout_modify.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.privacy_layout_modify:

                Intent intent = new Intent();
                intent.setClass(PrivacyActivity.this, FrogetPwdActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
