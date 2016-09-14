package izhipeng.dailybuy.myprefire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.home.HomeDetialActivity;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class MyCoinActivity extends BaseActivity {

    private LinearLayout ll_section_title_back;
    private RelativeLayout get_coin_layout;
    private String credits, dayCredits;
    private TextView my_coin_tv, mycoin_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coin);

        credits = getIntent().getStringExtra("credits");
        dayCredits = getIntent().getStringExtra("dayCredits");

        my_coin_tv = (TextView) findViewById(R.id.my_coin_tv);
        my_coin_tv.setText(credits);
        mycoin_tv = (TextView) findViewById(R.id.mycoin_tv);
        mycoin_tv.setText(dayCredits);
        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);

        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        get_coin_layout = (RelativeLayout) findViewById(R.id.get_coin_layout);
        get_coin_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(MyCoinActivity.this, HomeDetialActivity.class);
                intent.putExtra("webUrl", DailyBuyApplication.IP_URL + "getCreditsStrategy.jspa");
                startActivity(intent);
            }
        });
    }
}
