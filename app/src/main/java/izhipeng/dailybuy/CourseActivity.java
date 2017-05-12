package izhipeng.dailybuy;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.Timer;
import java.util.TimerTask;

import izhipeng.dailybuy.discover.DiscoverNew;
import izhipeng.dailybuy.home.HomeDetialActivity;
import izhipeng.dailybuy.home.HomeMain;
import izhipeng.dailybuy.home.LeftSlidingMenuFragment;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.library.slidingmenu.SlidingMenu;
import izhipeng.dailybuy.library.slidingmenu.app.SlidingFragmentActivity;
import izhipeng.dailybuy.myprefire.MyPreFireFragment;
import izhipeng.dailybuy.myprefire.SettingFragment;
import izhipeng.dailybuy.share.SocialShareFragment;
import izhipeng.dailybuy.widget.CircleImageView;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class CourseActivity extends SlidingFragmentActivity implements View.OnClickListener {

    public final static String FRAGMENT = "fragment";

    public final static int FRAGMENT_USERINFO = 1;
    public final static int FRAGMENT_HOME = 2;
    public final static int FRAGMENT_WINS = 3;
    public final static int FRAGMENT_LUCKEY = 4;
    public final static int FRAGMENT_SETTING = 5;
    public final static int FRAGMENT_SHARE = 6;
    private int fragment = FRAGMENT_HOME;

    public SlidingMenu mSlidingMenu;
    private Fragment mContent;
    private FragmentActivity mContent1;
    private LeftSlidingMenuFragment mFrag;
    private CircleImageView iv_main_left_head;

    private int excuse = 0;
    private Timer timer;
    private int time = 0;
    int id = -1;
    private LinearLayout ll_ivTitleBtnLeft;
    public static final String FRAGID = "fragid";

    public LinearLayout ll_section_title_back, ll_section_title_right;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.activity_course, null);
        setContentView(view);
        initView();
        initSlidingMenu();
        setLeftImg();
    }

    public void initView() {

        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(this);
        ll_section_title_right = (LinearLayout) findViewById(R.id.ll_section_title_right);
        ll_section_title_right.setOnClickListener(this);
        iv_main_left_head = (CircleImageView) findViewById(R.id.iv_main_left_head);
    }

    public void setLeftImg() {

        Glide.with(CourseActivity.this)
                .load(PreferencesUtil.get(DailyBuyApplication.KEY_URL, ""))
                .error(R.drawable.kuangge)
                .placeholder(R.drawable.kuangge)
                .dontAnimate()
                .into(iv_main_left_head);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void initSlidingMenu() {

        setBehindContentView(R.layout.main_left_layout);
        /**
         * 用来显示左边侧栏菜单的fragment
         */
        FragmentTransaction mFragementTransaction = getSupportFragmentManager()
                .beginTransaction();
        mFrag = new LeftSlidingMenuFragment();
        mFragementTransaction.replace(R.id.main_left_fragment, mFrag);

        mFragementTransaction.commit();
        mContent = new HomeMain();
        getSupportFragmentManager().beginTransaction().addToBackStack(null);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, mContent).commit();
        // customize the SlidingMenu
        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setMode(SlidingMenu.LEFT);// 设置是左滑还是右滑，还是左右都可以滑，我这里只做了左滑
        mSlidingMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);// 设置菜单宽度
        mSlidingMenu.setFadeDegree(0.35f);// 设置淡入淡出的比例
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);// 设置手势模式
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);// 设置左菜单阴影图片
        mSlidingMenu.setFadeEnabled(true);// 设置滑动时菜单的是否淡入淡出
        mSlidingMenu.setBehindScrollScale(0.333f);// 设置滑动时拖拽效果
        mSlidingMenu.setOnOpenedListener(new SlidingMenu.OnOpenedListener() {
            @Override
            public void onOpened() {
                AlphaAnimation animation = new AlphaAnimation(1.0f, 0.0f);
                animation.setDuration(800);
                //ivTitleBtnLeft.startAnimation(animation);
                //ivTitleBtnLeft.setVisibility(View.GONE);
            }
        });
        mSlidingMenu.setOnClosedListener(new SlidingMenu.OnClosedListener() {
            @Override
            public void onClosed() {
                AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
                animation.setDuration(800);
                //ivTitleBtnLeft.startAnimation(animation);
                //ivTitleBtnLeft.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_section_title_back:

                mSlidingMenu.showMenu(true);
                break;
            case R.id.ll_section_title_right:
                Intent intent = new Intent();
                intent.putExtra("webUrl", DailyBuyApplication.IP_URL + "ruleForIos.jspa");
                intent.setClass(CourseActivity.this, HomeDetialActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 左侧菜单点击切换首页的内容
     */
    public void switchContent(Fragment fragment) {
        mContent = fragment;
        getSupportFragmentManager().beginTransaction().addToBackStack(null);

        //// TODO: 2016/10/19 0019 隐藏显示
        /*if (!mContent.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .hide(fragment).add(R.id.content_frame, mContent)
                    .commitAllowingStateLoss();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .hide(fragment).show(mContent)
                    .commitAllowingStateLoss();
        }*/

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commitAllowingStateLoss();

        getSlidingMenu().showContent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        int tmpFragment = intent.getIntExtra(FRAGMENT, 0);
        if (tmpFragment > 0) {

            PreferencesUtil.put(DailyBuyApplication.CACHEKEY_MAIN_FRAGMENT, tmpFragment);
            fragment = tmpFragment;
            switch (fragment) {

                case FRAGMENT_HOME:
                    break;
                case FRAGMENT_WINS:
                    mContent = new DiscoverNew();
                    break;
                case FRAGMENT_LUCKEY:
                    mContent = new MyPreFireFragment();
                    break;
                case FRAGMENT_SETTING:
                    mContent = new SettingFragment();
                    break;
                case FRAGMENT_SHARE:
                    mContent = new SocialShareFragment();
                    break;
                default:
                    break;
            }
            switchContent(mContent);
        }
        super.onNewIntent(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            int count = getSupportFragmentManager().getBackStackEntryCount();
            if (getSupportFragmentManager().getBackStackEntryCount() == 0
                    && !mSlidingMenu.isMenuShowing()) {
                mSlidingMenu.showMenu(true);
                return true;
            }
            switch (PreferencesUtil.get(DailyBuyApplication.CACHEKEY_MAIN_FRAGMENT,
                    FRAGMENT_HOME)) {
                case FRAGMENT_HOME:
                    if (excuse == 0) {
                        Toast.makeText(getApplicationContext(), "再按一次退出",
                                Toast.LENGTH_SHORT).show();
                        excuse++;
                        timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                time++;
                            }
                        };
                        timer.schedule(task, 0, 1000);
                    } else {
                        if (time <= 10) {
                            CourseActivity.this.finish();
                        } else {
                            excuse = 0;
                            if (timer != null) {
                                timer.cancel();
                            }
                            time = 0;
                        }
                    }
                    break;
                default:
                    // fragment = FRAGMENT_COURSE;
                    // initSlidingMenu();
                    // tv_main_title_TitleBtnRigh.setVisibility(View.GONE);
                    // PreferencesUtil.put(NCE2.CACHEKEY_MAIN_FRAGMENT,
                    // FRAGMENT_COURSE);

                    fragment = FRAGMENT_HOME;
                    // initSlidingMenu();
                    // tv_main_title_TitleBtnRigh.setVisibility(View.GONE);
                    // PreferencesUtil.put(NCE2.CACHEKEY_MAIN_FRAGMENT,
                    // FRAGMENT_COURSE);

                    if (excuse == 0) {
                        Toast.makeText(getApplicationContext(), "再按一次退出",
                                Toast.LENGTH_SHORT).show();
                        excuse++;
                        timer = new Timer();
                        TimerTask task = new TimerTask() {
                            @Override
                            public void run() {
                                time++;
                            }
                        };
                        timer.schedule(task, 0, 1000);
                    } else {
                        if (time <= 10) {
                            CourseActivity.this.finish();
                        } else {
                            excuse = 0;
                            if (timer != null) {
                                timer.cancel();
                            }
                            time = 0;
                        }
                    }
                    break;
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
