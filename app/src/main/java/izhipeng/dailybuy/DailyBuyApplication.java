package izhipeng.dailybuy;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.tencent.android.tpush.XGNotifaction;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.XGPushNotifactionCallback;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;


import java.util.List;
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
    public static final String KEY_AMOUNT = "key-amount";
    public static final String CACHEKEY_MAIN_FRAGMENT = "cachekey-main_fragment";
    public static final String SHAREPREFERENCE_NAME = "DailyBuyApplication";
    public static final String TENCENT_APP_ID = "101349957";
    public static final String TENCENT_APP_SECRET = "3fc37bbd87fd2f898c53522fc26db0f3";
    public static final String WX_APP_ID = "wx3ddafc9c0b621abb";
    public static final String WX_APP_SCRET = "d7913b08c32861510d69b2e344d938c8";
    public static final String SINA_APP_KEY = "1993697517";
    public static final String SINA_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    public static final String SINA_SCOPE = "all";
    public static IWXAPI api;

    public static String IP_URL = "http://210.51.190.27:8086/";

    public static final String CACHEKEY_USERINFO = "cachekey-userinfo-1";


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

    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //regToWx(this);
        context = getApplicationContext();
        //preferences = PreferenceManager.getDefaultSharedPreferences(context);
        //允许单双线程并行
        preferences = getSharedPreferences(SHAREPREFERENCE_NAME, Context.MODE_PRIVATE |Context.MODE_MULTI_PROCESS);

        displayMetrics = getResources().getDisplayMetrics();
        // 初始化屏幕宽度缩放比例
        screenWidthScale = displayMetrics.widthPixels / UI_Design_Width;
        screenHeightScale = displayMetrics.heightPixels / UI_Design_Height;

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                //.addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                        //其他配置
                .build();

        OkHttpUtils.initClient(okHttpClient);

        if (isMainProcess()) {

            XGPushManager
                    .setNotifactionCallback(new XGPushNotifactionCallback() {
                        @Override
                        public void handleNotify(XGNotifaction xGNotifaction) {
                            Log.i("test", "处理信鸽通知：" + xGNotifaction);
                            // 获取标签、内容、自定义内容
                            String title = xGNotifaction.getTitle();
                            String content = xGNotifaction.getContent();
                            String customContent = xGNotifaction
                                    .getCustomContent();
                            // 其它的处理
                            // 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
                            xGNotifaction.doNotify();
                        }
                    });
        }
    }


    //uemng平台配置方法块
    {
        //微信
        PlatformConfig.setWeixin(DailyBuyApplication.WX_APP_ID, DailyBuyApplication.WX_APP_SCRET);
        //新浪微博
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad");
        //QQ
        PlatformConfig.setQQZone(DailyBuyApplication.TENCENT_APP_ID, DailyBuyApplication.TENCENT_APP_SECRET);
    }

    private void regToWx(Context context) {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(context, DailyBuyApplication.WX_APP_ID, true);
        // 将应用的appId注册到微信
        api.registerApp(DailyBuyApplication.WX_APP_ID);
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
