package izhipeng.dailybuy.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;

import java.util.Stack;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.login.LoginMainActivity;
import izhipeng.dailybuy.login.SelectActivity;
import izhipeng.dailybuy.serach.SerachActivity;
import izhipeng.dailybuy.share.ShareActivity;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class HomeDetialActivity extends BaseActivity {

    private String webUrl;
    private int state;
    private WebView mWebView;
    private LinearLayout ll_section_title_back;
    private TextView tv_section_title_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_weburl_test);
        webUrl = getIntent().getStringExtra("webUrl");
        state = getIntent().getIntExtra("state", 0);

        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_section_title_title = (TextView) findViewById(R.id.tv_section_title_title);

        mWebView = (WebView) findViewById(R.id.webView);
        //mWebView.loadUrl(webUrl + "&state=" + state);
        mWebView.loadUrl(webUrl);
        //在js中调用本地java方法
        mWebView.addJavascriptInterface(new JsInterface(HomeDetialActivity.this), "AndroidWebView");
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

            /**
             * 记录URL的栈
             * 规则:
             * 1.不可在{@code WebView.onPageFinished();}中开始记录URL;
             * 2.记录需要屏蔽重定向URL.
             */
            private final Stack<String> mUrls = new Stack<>();
            /**
             * 判断页面是否加载完成
             */
            private boolean mIsLoading;
            private String mUrlBeforeRedirect;

            /**
             * 记录非重定向链接, 避免刷新页面造成的重复入栈
             *
             * @param url 链接
             */
            private void recordUrl(String url) {
                //这里还可以根据自身业务来屏蔽一些链接被放入URL栈
                if (!TextUtils.isEmpty(url) && !url.equalsIgnoreCase(getLastPageUrl())) {
                    mUrls.push(url);
                } else if (!TextUtils.isEmpty(mUrlBeforeRedirect)) {
                    mUrls.push(mUrlBeforeRedirect);
                    mUrlBeforeRedirect = null;
                }
            }

            /**
             * 获取上一页的链接
             **/
            private synchronized String getLastPageUrl() {
                return mUrls.size() > 0 ? mUrls.peek() : null;
            }

            /**
             * 推出上一页链接
             */
            public String popLastPageUrl() {
                if (mUrls.size() >= 2) {
                    mUrls.pop();//pop current page url
                    return mUrls.pop();
                }
                return null;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //view.loadUrl(url);

                return false;
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
                if (mIsLoading && mUrls.size() > 0) {
                    mUrlBeforeRedirect = mUrls.pop();
                }
                recordUrl(url);
                this.mIsLoading = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (this.mIsLoading || url.startsWith("about:")) {
                    this.mIsLoading = false;
                }
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
        webSettings.setUseWideViewPort(true);// 这个很关键
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

        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        //在js中调用window.AndroidWebView.showInfoFromJs(name)，便会触发此方法。
        @JavascriptInterface
        public void toShare(String infoTitle, String infoImg, String webUrl, String activeContent, String infoid) {
            //Toast.makeText(mContext, name + desp + imageUrl + webUrl, Toast.LENGTH_SHORT).show();

            if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {

                Intent intent = new Intent();
                intent.setClass(HomeDetialActivity.this, SelectActivity.class);
                startActivity(intent);
            } else {
                showToast(infoTitle + infoImg + webUrl + activeContent + infoid, 1000);
                Intent intent = new Intent();
                intent.putExtra("shareTitle", infoTitle);
                intent.putExtra("shareDesp", infoImg);
                intent.putExtra("shareImageUrl", webUrl);
                intent.putExtra("shareWebUrl", activeContent);
                intent.putExtra("infoId", infoid);
                intent.setClass(HomeDetialActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        }

        @JavascriptInterface
        public void toFavor(String infotitle) {
            if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {
                Log.i("tag_webview", infotitle);
                Intent intent = new Intent();
                intent.setClass(HomeDetialActivity.this, SelectActivity.class);
                startActivity(intent);
            }

        }

        @JavascriptInterface
        public void toComment(String infotitle) {

            if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {
                Log.i("tag_webview", infotitle);
                Intent intent = new Intent();
                intent.setClass(HomeDetialActivity.this, SelectActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.reload();
        //mWebView.loadUrl(webUrl + "&userId=" + PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""));
        Log.i("tag_111", "onResume");
    }
}
