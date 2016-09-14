package izhipeng.dailybuy.discover;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.calendarview.CalendarSelecterView;
import izhipeng.dailybuy.calendarview.MonthChangeListenr;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.signcalender.DPCManager;
import izhipeng.dailybuy.signcalender.DatePicker;
import izhipeng.dailybuy.signcalender.DatePicker2;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class SignActivity extends BaseActivity {

    private ViewPager mViewPager;
    private LinearLayout ll_section_title_back;
    private TextView tv_section_title_title;
    private ImageView sign_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign);
        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_section_title_title = (TextView) findViewById(R.id.tv_section_title_title);
        tv_section_title_title.setText("签到");

        sign_img = (ImageView) findViewById(R.id.sign_img);
        Glide.with(this)
                .load(PreferencesUtil.get(DailyBuyApplication.KEY_URL, ""))
                .dontTransform()
                .dontAnimate()
                .error(R.drawable.avatar_default)
                .placeholder(R.drawable.avatar_default)
                .into(sign_img);

        mViewPager = (ViewPager) findViewById(R.id.calendar_pager);
        mViewPager.setAdapter(new CalendarAdapter());
        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
    }

    MonthChangeListenr mMonthChangeListenr = new MonthChangeListenr() {

        @Override
        public void LastMonthClicked() {
            mViewPager.arrowScroll(View.FOCUS_LEFT);
        }

        @Override
        public void nextMonthClicker() {
            mViewPager.arrowScroll(View.FOCUS_RIGHT);

        }

    };

    class CalendarAdapter extends PagerAdapter {
        CalendarSelecterView[] views = new CalendarSelecterView[5];
        private Calendar mCalendar;

        public CalendarAdapter() {
            for (int i = 0; i < 5; i++) {
                views[i] = new CalendarSelecterView(getBaseContext());
                views[i].setOnMonthChangeListenr(mMonthChangeListenr);
            }
            mCalendar = Calendar.getInstance();
        }

        @Override
        public int getItemPosition(Object object) {
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            CalendarSelecterView v = views[position % 5];
            int months = Integer.MAX_VALUE / 2 - position;
            Calendar calendar = (Calendar) mCalendar.clone();
            calendar.add(Calendar.MONTH, -(months));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            v.setTime(year, month, day, false);
            container.addView(v);
            return v;
        }
    }
}
