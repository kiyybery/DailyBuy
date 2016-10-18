package izhipeng.dailybuy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.tencent.android.tpush.common.Constants;
import com.tencent.android.tpush.service.XGPushService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import izhipeng.dailybuy.bean.XGNotification;
import izhipeng.dailybuy.discover.DiscoverNew;
import izhipeng.dailybuy.home.HomeMain;
import izhipeng.dailybuy.library.TabEntity;
import izhipeng.dailybuy.library.ViewFindUtils;
import izhipeng.dailybuy.message.MessageParentFrag;
import izhipeng.dailybuy.myprefire.MyPreFireFragment;

public class MainActivity extends AppCompatActivity {

    private HomeMain homeMain;
    //    private DiscoverMain discoverMainFrag;
    private DiscoverNew discoverNew;
    private MessageParentFrag messageParentFrag;
    private MyPreFireFragment myProfileFragment;
    private Fragment[] mFragments;

    FragmentManager fm;

    private int[] iconResid =
            {R.drawable.footer_home_off,
                    R.drawable.footer_discover_off,
                    R.drawable.footer_msg_off,
                    R.drawable.footer_me_off};

    private int[] iconResidClick =
            {R.drawable.footer_home_on,
                    R.drawable.footer_discover_on,
                    R.drawable.footer_msg_on,
                    R.drawable.footer_me_on};
    private String[] titles = {"首页", "发现", "消息", "我的"};
    private ArrayList<Fragment> fragments2 = new ArrayList<>();
    private ArrayList<CustomTabEntity> tabs = new ArrayList<>();
    private CommonTabLayout tl;
    private View decorView;
    private Context context;
    Message m = null;
    String DEVICE_ID;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        DEVICE_ID = tm.getDeviceId();

        XGPushConfig.enableDebug(this, true);

        Handler handler = new HandlerExtension(MainActivity.this);
        m = handler.obtainMessage();

        XGPushManager.registerPush(getApplicationContext(),
                new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object data, int flag) {
                        Log.w(Constants.LogTag,
                                "+++ register push sucess. token:" + data);
                        m.obj = "+++ register push sucess. token:" + data;
                        m.sendToTarget();
                    }

                    @Override
                    public void onFail(Object data, int errCode, String msg) {
                        Log.w(Constants.LogTag,
                                "+++ register push fail. token:" + data
                                        + ", errCode:" + errCode + ",msg:"
                                        + msg);

                        m.obj = "+++ register push fail. token:" + data
                                + ", errCode:" + errCode + ",msg:" + msg;
                        m.sendToTarget();
                    }
                });

        Intent service = new Intent(context, XGPushService.class);
        context.startService(service);
        if (savedInstanceState == null) {
            //initFragment();

            for (int i = 0; i < titles.length; i++) {
                tabs.add(new TabEntity(titles[i], iconResidClick[i], iconResid[i]));
            }
            fragments2.add(homeMain.newInstance());
            fragments2.add(discoverNew.newInstance());
            fragments2.add(messageParentFrag.newInstance());
            fragments2.add(myProfileFragment.newInstance());

            decorView = getWindow().getDecorView();
            tl = ViewFindUtils.find(decorView, R.id.tl_2);
            tl.setTabData(tabs, this, R.id.home_fragment_container, fragments2);
        }
    }

    private void initFragment() {

        homeMain = HomeMain.newInstance();
//        discoverMainFrag = DiscoverMain.newInstance();
        discoverNew = DiscoverNew.newInstance();
        messageParentFrag = new MessageParentFrag();
        myProfileFragment = MyPreFireFragment.newInstance();
        //myProfileFragment = MyProFileFragment1.newInstance();
        mFragments = new Fragment[]{homeMain, discoverNew, messageParentFrag, myProfileFragment};
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.home_fragment_container, homeMain);
        ft.add(R.id.home_fragment_container, discoverNew);
        ft.add(R.id.home_fragment_container, messageParentFrag);
        ft.add(R.id.home_fragment_container, myProfileFragment);
        ft.hide(myProfileFragment).hide(messageParentFrag).hide(discoverNew).show(homeMain);
        ft.commit();
//  TODO   看看不判断就会有什么影响    if (savedInstanceState == null) { }
    }

    @Override
    public void onBackPressed() {
        fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        } else {
            moveTaskToBack(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        XGPushClickedResult click = XGPushManager.onActivityStarted(this);
        Log.d("TPush", "onResumeXGPushClickedResult:" + click);
        if (click != null) { // 判断是否来自信鸽的打开方式
            Toast.makeText(this, "通知被点击:" + click.toString(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        XGPushManager.onActivityStoped(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        XGPushManager.unregisterPush(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private static class HandlerExtension extends Handler {
        WeakReference<MainActivity> mActivity;

        HandlerExtension(MainActivity activity) {
            mActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity theActivity = mActivity.get();
            if (theActivity == null) {
                theActivity = new MainActivity();
            }
            if (msg != null) {
                Log.w(Constants.LogTag, msg.obj.toString());
                //Log.i("push_token", XGPushConfig.getToken(theActivity));
            }
        }
    }
}
