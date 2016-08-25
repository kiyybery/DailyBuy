package izhipeng.dailybuy.myprefire;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.R;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class MyCoinActivity extends BaseActivity {

    private LinearLayout ll_section_title_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coin);

        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);

        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
