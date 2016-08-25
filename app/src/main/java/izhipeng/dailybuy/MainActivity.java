package izhipeng.dailybuy;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }
}
