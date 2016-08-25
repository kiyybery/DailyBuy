package izhipeng.dailybuy.myprefire;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.R;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class AboutActivity extends BaseActivity {

    private TextView mCallPhone_tv, mTitle, vTextView;
    private LinearLayout mBack;
    Dialog dialog;
    private RelativeLayout info_layout, deal_layout;
    private LinearLayout ll_section_title_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        mBack = (LinearLayout) findViewById(R.id.ll_section_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mTitle = (TextView) findViewById(R.id.tv_section_title_title);
        mTitle.setText("关于我们");
    }
}

