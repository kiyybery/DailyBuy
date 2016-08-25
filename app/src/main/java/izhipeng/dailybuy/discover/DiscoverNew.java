package izhipeng.dailybuy.discover;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import izhipeng.dailybuy.BaseFragment;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.login.LoginMainActivity;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class DiscoverNew extends BaseFragment implements View.OnClickListener {

    private WebView mWebView;
    private ImageView mLeft_iv;
    private RelativeLayout mTitle_layout;
    private TextView mTitle;
    private LinearLayout ll_section_title_right;

    public static DiscoverNew newInstance() {

        DiscoverNew f = new DiscoverNew();
        Bundle data = new Bundle();
        data.putString("title", "首页");
        f.setArguments(data);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);
        mLeft_iv = (ImageView) view.findViewById(R.id.iv_section_title_back);
        mLeft_iv.setVisibility(View.GONE);
        mTitle_layout = (RelativeLayout) view.findViewById(R.id.rl_section_title);
        //mTitle_layout.setBackgroundColor(0xFFFF0000);
        mTitle = (TextView) view.findViewById(R.id.tv_section_title_title);
        mTitle.setText("发现");
        mTitle.setTextColor(0xffffffff);
        ll_section_title_right = (LinearLayout) view.findViewById(R.id.ll_section_title_right);
        ll_section_title_right.setOnClickListener(this);
        mWebView = (WebView) view.findViewById(R.id.webView);
        mWebView.setVerticalScrollbarOverlay(true);
        mWebView.loadUrl(DailyBuyApplication.IP_URL + "favorableListByUser.jspa?uType=1&pageId=1&userId=e2os3NIaWaA=");
        startProgressBar("加载中...", new Thread(), true);
        //在js中调用本地java方法
        mWebView.addJavascriptInterface(new JsInterface(getActivity()), "AndroidWebView");
        //mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.setWebChromeClient(new WebChromeClient() {

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
                view.loadUrl(url);
                return true;
                // 需要return true or false ，不然会重新请求
                //return super.shouldOverrideUrlLoading(view, url);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_section_title_right:


                break;
            default:
                break;
        }
    }

    private class JsInterface {

        private Context mContext;

        public JsInterface(Context context) {
            this.mContext = context;
        }
    }

    public void sendUidToJs(View view) {

        String userId = PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, "");
        if (TextUtils.isEmpty(userId)) {

            Intent intent = new Intent();
            intent.setClass(getActivity(), LoginMainActivity.class);
            startActivity(intent);
        } else {

            mWebView.loadUrl("javascript:showInfoFromAndroid('" + userId + "')");
        }
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

    @Override
    public void onPause() {
        super.onPause();
    }
}
