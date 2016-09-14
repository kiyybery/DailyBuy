package izhipeng.dailybuy.myprefire;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.home.CommentActivity;
import izhipeng.dailybuy.home.HomeDetialActivity;
import izhipeng.dailybuy.library.PreferencesUtil;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/9/12 0012.
 */
public class OnLineGiftWebView extends BaseActivity {

    private String webUrl;
    private WebView mWebView;
    private LinearLayout ll_section_title_back, ll_section_title_right;
    private TextView tv_section_title_title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webUrl = getIntent().getStringExtra("webUrl");
        setContentView(R.layout.activity_onlinewebview);

        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_section_title_right = (LinearLayout) findViewById(R.id.ll_section_title_right);
        ll_section_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.putExtra("webUrl", DailyBuyApplication.IP_URL + "exchangeRecordList.jspa?userId="
                        + PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""));
                intent.setClass(OnLineGiftWebView.this, HomeDetialActivity.class);
                startActivity(intent);
            }
        });

        tv_section_title_title = (TextView) findViewById(R.id.tv_section_title_title);

        mWebView = (WebView) findViewById(R.id.webView_onlinegift);
        mWebView.loadUrl(webUrl);

        //在js中调用本地java方法
        mWebView.addJavascriptInterface(new JsInterface(OnLineGiftWebView.this), "AndroidWebView");
        //mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                //mTitle.setText(title);
                tv_section_title_title.setText(title);
                super.onReceivedTitle(view, title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                if (newProgress == 100) {
                    closeProgressBar();
                } else {
                    startProgressBar("加载中...", new Thread(), true);
                }
                super.onProgressChanged(view, newProgress);
            }
        });

        mWebView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);

                Intent intent = new Intent();
                intent.putExtra("webUrl", url);
                intent.setClass(OnLineGiftWebView.this, CommentActivity.class);
                startActivity(intent);
                return true;
            }

            @Override
            public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                super.onFormResubmission(view, dontResend, resend);
            }

            @Override
            public void onScaleChanged(WebView view, float oldScale, float newScale) {
                super.onScaleChanged(view, oldScale, newScale);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
            }


            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                super.onReceivedError(view, errorCode, description, failingUrl);

                //这里进行无网络或错误处理，具体可以根据errorCode的值进行判断，做跟详细的处理。
                //mWebView.loadUrl(" file:///android_asset/loading.html ");
            }
        });

        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSupportZoom(true);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setBuiltInZoomControls(true);//support zoom
        //webSettings.setPluginsEnabled(true);//support flash
        webSettings.setUseWideViewPort(true);// 这
        webSettings.setLoadWithOverviewMode(true);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if (mDensity == 120) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        } else if (mDensity == DisplayMetrics.DENSITY_XHIGH) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == DisplayMetrics.DENSITY_TV) {
            webSettings.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }
    }

    private class JsInterface {

        private Context context;

        public JsInterface(Context context) {
            this.context = context;
        }
    }
}
