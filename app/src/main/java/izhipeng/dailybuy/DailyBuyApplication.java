package izhipeng.dailybuy;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import net.tsz.afinal.FinalDb;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class DailyBuyApplication extends Application {

    public static Context context;
    public static SharedPreferences preferences;
    public static final String KEY_AUTH = "key-auth";
    public static final String KEY_NAME = "key-name";
    public static final String KEY_PASSWORD = "kry-password";
    public static final String KEY_SEX = "key-sex";
    public static final String KEY_CITY = "key-city";
    public static final String KEY_CREDITS = "key-credits";
    public static final String KEY_TYPE = "key-type";
    public static final String KEY_PATH = "key-path";
    public static final String KEY_URL = "key-url";
    public static final String TENCENT_APP_ID = "1105534386";
    public static final String TENCENT_APP_SECRET = "McDfT7Dlh4JbPphD";
    public static final String WX_APP_ID = "wx0e45a1d33a58bd2f";
    public static final String SINA_APP_KEY = "1993697517";
    public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String SINA_SCOPE = "all";

    public static String IP_URL = "http://210.51.190.27:8086/";

    public static final String CACHEKEY_USERINFO = "cachekey-userinfo-1";

    public static FinalDb db;

    public static DisplayMetrics displayMetrics;
    // 原始UI界面设计图的宽度(px)，用于后期对控件做缩放
    public static final float UI_Design_Width = 720;
    public static final float UI_Design_Height = 1136;
    // 屏幕宽度缩放比（相对于原设计图）
    public static float screenWidthScale = 1f;
    public static float screenHeightScale = 1f;

    private static DailyBuyApplication app;

    public static DailyBuyApplication get() {

        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
        db = FinalDb.create(context, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(context);

        displayMetrics = getResources().getDisplayMetrics();
        // 初始化屏幕宽度缩放比例
        screenWidthScale = displayMetrics.widthPixels / UI_Design_Width;
        screenHeightScale = displayMetrics.heightPixels / UI_Design_Height;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                        //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);
    }

    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context
                    .getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(
                        context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}