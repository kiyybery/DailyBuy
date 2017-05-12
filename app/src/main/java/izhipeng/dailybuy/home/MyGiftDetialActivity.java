package izhipeng.dailybuy.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.share.ShareActivity;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class MyGiftDetialActivity extends BaseActivity {

    private ImageView detial_gift_img;
    private TextView detial_gift_name, detial_gift_money, detial_gift_num,
            exchange_time_tv, expressage_from_tv, order_id_tv, status_tv, tv_section_title_title;
    private LinearLayout ll_section_title_back;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detial);

        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_section_title_title = (TextView) findViewById(R.id.tv_section_title_title);
        tv_section_title_title.setText("订单详情");

        detial_gift_img = (ImageView) findViewById(R.id.detial_gift_img);
        detial_gift_name = (TextView) findViewById(R.id.detial_gift_name);
        detial_gift_money = (TextView) findViewById(R.id.detial_gift_money);
        detial_gift_num = (TextView) findViewById(R.id.detial_gift_num);
        exchange_time_tv = (TextView) findViewById(R.id.exchange_time_tv);
        expressage_from_tv = (TextView) findViewById(R.id.expressage_from_tv);
        order_id_tv = (TextView) findViewById(R.id.order_id_tv);
        status_tv = (TextView) findViewById(R.id.status_tv);

        Glide
                .with(this)
                .load(getIntent().getStringExtra("giftImage"))
                .dontAnimate()
                .into(detial_gift_img);

        detial_gift_name.setText(getIntent().getStringExtra("giftName"));
        detial_gift_money.setText(getIntent().getStringExtra("price"));
        detial_gift_num.setText(getIntent().getIntExtra("number", 0) + "");
        exchange_time_tv.setText(getIntent().getStringExtra("createTime"));
        expressage_from_tv.setText(getIntent().getStringExtra("expressage"));
        order_id_tv.setText(getIntent().getStringExtra("expressNumber"));
        if (getIntent().getIntExtra("tradingStatus", 0) == 1) {

            status_tv.setText("待发货");

        } else if (getIntent().getIntExtra("tradingStatus", 0) == 2) {

            status_tv.setText("发货中");
        } else if (getIntent().getIntExtra("tradingStatus", 0) == 3) {

            status_tv.setText("交易完成");
        }
    }
}
