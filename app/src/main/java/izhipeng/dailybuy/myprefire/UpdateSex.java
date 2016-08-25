package izhipeng.dailybuy.myprefire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class UpdateSex extends BaseActivity implements View.OnClickListener {

    private ImageView mCheckMan, mCheckWoman;
    private LinearLayout mBack, ll_section_title_right;
    private TextView mtitle, right_tv, mMan_tv, mWoman_tv;
    private int sex;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatesex);

        mCheckMan = (ImageView) findViewById(R.id.right_check_man);
        mCheckWoman = (ImageView) findViewById(R.id.right_check_woman);

        mCheckMan.setOnClickListener(this);
        mCheckWoman.setOnClickListener(this);

        mMan_tv = (TextView) findViewById(R.id.man_tv);
        mMan_tv.setOnClickListener(this);
        mWoman_tv = (TextView) findViewById(R.id.woman_tv);
        mWoman_tv.setOnClickListener(this);

        mBack = (LinearLayout) findViewById(R.id.ll_section_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mtitle = (TextView) findViewById(R.id.tv_section_title_title);
        mtitle.setText("性别");
        ll_section_title_right = (LinearLayout) findViewById(R.id.ll_section_title_right);
        ll_section_title_right.setVisibility(View.VISIBLE);
        right_tv = (TextView) findViewById(R.id.tv_section_title_right);
        right_tv.setText("完成");
        right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PreferencesUtil.put(DailyBuyApplication.KEY_SEX, sex);
                Intent intent = new Intent();
                intent.putExtra("sex", sex);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.man_tv:
                sex = 2;
                mCheckMan.setVisibility(View.VISIBLE);
                mCheckWoman.setVisibility(View.GONE);
                break;

            case R.id.woman_tv:
                sex = 1;
                mCheckMan.setVisibility(View.GONE);
                mCheckWoman.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }
}
