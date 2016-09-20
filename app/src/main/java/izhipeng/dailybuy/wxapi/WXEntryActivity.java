package izhipeng.dailybuy.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.json.JSONObject;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import cz.msebera.android.httpclient.Header;

/**
 * Created by Administrator on 2016/8/9 0009.
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private static final String TAG = "WXEntryActivity";

    private AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        DailyBuyApplication.api.handleIntent(getIntent(), this);

    }

    @Override
    public void onReq(BaseReq baseReq) {
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        DailyBuyApplication.api.handleIntent(getIntent(), this);
        finish();
    }

    @Override
    public void onResp(BaseResp resp) {
        SendAuth.Resp resp1 = (SendAuth.Resp) resp;
        switch (resp1.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                Toast.makeText(this, "用户拒绝授权", Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                Toast.makeText(this, "用户取消", Toast.LENGTH_LONG).show();
                break;
            case BaseResp.ErrCode.ERR_OK:

                switch (resp1.getType()) {
                    case RETURN_MSG_TYPE_SHARE:
                        finish();
                        break;

                    case RETURN_MSG_TYPE_LOGIN:
                        //Toast.makeText(this, "微信登录返回", Toast.LENGTH_LONG).show();
                        // 获取到code
                        //String code = ((SendAuth.Resp) resp).code;
                        String code = resp1.code;
                        String url = "https://api.weixin.qq.com/sns/oauth2/access_token?" +
                                "appid=" + DailyBuyApplication.WX_APP_ID +
                                "&secret=d7913b08c32861510d69b2e344d938c8" +
                                "&code=" + code +
                                "&grant_type=authorization_code";
                        client.get(url, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);
                                if (response != null) {
                                    try {
                                        String unionid = response.getString("unionid");
                                        String openid = response.getString("openid");
                                        String refresh_token = response.getString("refresh_token");
                                        String expires_in = response.getString("expires_in");
                                        String access_token = response.getString("access_token");

                                        System.out.println(unionid + "===" + openid + "===" + refresh_token + "===" + expires_in + "===" + access_token);


                                        client.get("https://api.weixin.qq.com/sns/userinfo?access_token=" + access_token +
                                                "&openid=" + openid, new JsonHttpResponseHandler() {
                                            @Override
                                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                                super.onSuccess(statusCode, headers, response);

                                                if (response != null) {
                                                    try {
                                                        String nickname = response.getString("nickname");
                                                        String sex = response.getString("sex");
                                                        String province = response.getString("province");
                                                        String city = response.getString("city");
                                                        String country = response.getString("country");
                                                        String headimgurl = response.getString("headimgurl");

                                                        String info = "nickname:" + nickname +
                                                                " sex:" + sex +
                                                                " procince:" + province +
                                                                " city:" + city +
                                                                " country:" + country +
                                                                " headimagurl" + headimgurl;
                                                        Toast.makeText(getApplicationContext(), info, Toast.LENGTH_LONG).show();

                                                        Log.i("wx_info", info);
                                                        finish();
                                                    } catch (Exception e) {

                                                    }
                                                }
                                            }
                                        });

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                        finish();
                        break;
                }
                break;
        }
    }


}
