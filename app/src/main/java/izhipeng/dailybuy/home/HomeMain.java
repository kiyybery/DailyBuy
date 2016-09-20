package izhipeng.dailybuy.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Stack;

import izhipeng.dailybuy.BaseFragment;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.login.LoginMainActivity;
import izhipeng.dailybuy.login.SelectActivity;
import izhipeng.dailybuy.publish.PublishActivity;
import izhipeng.dailybuy.publish.PublishStoreActivity;
import izhipeng.dailybuy.serach.SerachActivity;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class HomeMain extends BaseFragment implements View.OnClickListener {

    private WebView mWebView;
    private ImageView mLeft_iv, mRight_iv;
    private RelativeLayout mTitle_layout;
    private TextView mTitle;
    private int uType = 2;
    private LinearLayout ll_section_title_back, home_select_layout;
    private int selectedPosition = 0;
    private TextView select_all, select_new;


    public static HomeMain newInstance() {

        HomeMain f = new HomeMain();
        Bundle data = new Bundle();
        data.putString("title", "首页");
        f.setArguments(data);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mLeft_iv = (ImageView) view.findViewById(R.id.iv_section_title_back);
        mRight_iv = (ImageView) view.findViewById(R.id.iv_section_icon_right);
        mLeft_iv.setOnClickListener(this);
        mRight_iv.setOnClickListener(this);
        home_select_layout = (LinearLayout) view.findViewById(R.id.home_select_layout);
        mTitle_layout = (RelativeLayout) view.findViewById(R.id.rl_section_title);
        mTitle = (TextView) view.findViewById(R.id.tv_section_title_title);
        mTitle.setOnClickListener(this);
        select_all = (TextView) view.findViewById(R.id.select_all);
        select_all.setOnClickListener(this);
        select_new = (TextView) view.findViewById(R.id.select_new);
        select_new.setOnClickListener(this);
        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.setVerticalScrollbarOverlay(true);
        //if (!checkLogin()) {
        mWebView.loadUrl(DailyBuyApplication.IP_URL + "favorableListByBuss.jspa?pageId=1&uType=2&userId="
                + PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""));
        //}
        startProgressBar("加载中...", new Thread(), true);
        //在js中调用本地java方法
        mWebView.addJavascriptInterface(new JsInterface(getActivity()), "AndroidWebView");
        //mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public void onReceivedTitle(WebView view, String title) {
                mTitle.setText(title);
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
                //return true;
                // 需要return true or false ，不然会重新请求
                Log.i("log_url", url);

                if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {

                    Intent intent = new Intent(getActivity(), SelectActivity.class);
                    startActivity(intent);
                    return true;
                } else {
                    Intent intent = new Intent(getActivity(), HomeDetialActivity.class);
                    intent.putExtra("webUrl", url);
                    startActivity(intent);
                    return true;
                }
                //view.stopLoading();
                //mWebView.stopLoading();
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
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
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

        return view;
    }


    private class JsInterface {

        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }

        @JavascriptInterface
        public void toBanner(String url) {

            Log.i("redicturl", url);
            Toast.makeText(mContext, "toBanner", Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.putExtra("webUrl", url);
            intent.setClass(getActivity(), HomeDetialActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_section_icon_right:
                if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), SelectActivity.class);
                    startActivity(intent);
                } else {
                    if (PreferencesUtil.get(DailyBuyApplication.KEY_TYPE, 0) == 1) {

                        Intent intent = new Intent();
                        intent.putExtra("uType", 1);
                        intent.setClass(getActivity(), PublishActivity.class);
                        startActivity(intent);
                    } else {

                        Intent intent = new Intent();
                        intent.putExtra("uType", 2);
                        intent.setClass(getActivity(), PublishStoreActivity.class);
                        startActivity(intent);
                    }
                }
                break;
            case R.id.iv_section_title_back:

                Intent intent = new Intent();
                intent.setClass(getActivity(), SerachActivity.class);
                startActivity(intent);
                break;

            case R.id.tv_section_title_title:
                if (selectedPosition % 2 == 0) {

                    home_select_layout.setVisibility(View.VISIBLE);
                } else {

                    home_select_layout.setVisibility(View.GONE);
                }
                selectedPosition++;
                break;

            case R.id.select_all:

                showToast("all", 1000);
                mWebView.loadUrl(DailyBuyApplication.IP_URL + "favorableListByBuss.jspa?pageId=1&uType=2&userId=e2os3NIaWaA=");
                home_select_layout.setVisibility(View.GONE);
                //mTitle.setText("首页");
                break;
            case R.id.select_new:

                showToast("new", 1000);
                mWebView.loadUrl(DailyBuyApplication.IP_URL + "favorableListByBuss.jspa?pageId=1&uType=2&userId=e2os3NIaWaA=&selection=1");
                home_select_layout.setVisibility(View.GONE);
                //mTitle.setText("精选");
                break;
            default:
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
