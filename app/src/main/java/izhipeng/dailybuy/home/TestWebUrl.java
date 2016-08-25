package izhipeng.dailybuy.home;

import android.os.Bundle;
import android.webkit.WebView;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.R;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class TestWebUrl extends BaseActivity{

    private String webUrl;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weburl_test);
        webUrl = getIntent().getStringExtra("webUrl");
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(webUrl);
    }
}
