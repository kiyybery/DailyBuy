package izhipeng.dailybuy.myprefire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class UpdateUserName extends BaseActivity {

    private EditText update_name_et;
    private LinearLayout ll_section_title_right, ll_section_title_back;
    private TextView mtitle, right_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatename);
        update_name_et = (EditText) findViewById(R.id.update_name_et);

        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mtitle = (TextView) findViewById(R.id.tv_section_title_title);
        mtitle.setText("修改昵称");
        ll_section_title_right = (LinearLayout) findViewById(R.id.ll_section_title_right);
        ll_section_title_right.setVisibility(View.VISIBLE);
        right_tv = (TextView) findViewById(R.id.tv_section_title_right);
        right_tv.setText("完成");
        right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferencesUtil.put(DailyBuyApplication.KEY_NAME, update_name_et.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("username", update_name_et.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
}
