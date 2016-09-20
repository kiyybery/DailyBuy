package izhipeng.dailybuy.discover;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.SignTimeList;
import izhipeng.dailybuy.calendarview.SignCalendar;
import izhipeng.dailybuy.library.PreferencesUtil;
import okhttp3.Call;


/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class SignActivity extends BaseActivity implements SignCalendar.OnCalendarClickListener {

    //    private ViewPager mViewPager;
    private LinearLayout ll_section_title_back;
    private TextView tv_section_title_title;
    private ImageView sign_img;
    private SignCalendar mSignCalendar;
    private String data;
    private String userId;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign);
        userId = PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, "");
        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        OkHttpUtils.get()
                .url(DailyBuyApplication.IP_URL + "getSignInfo.jspa")
                .addParams("userId", userId)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("SignActivity", response);
                        Gson gson = new Gson();
                        SignTimeList signTimeList = gson.fromJson(response, SignTimeList.class);
                        List<Long> signTimeList1 = signTimeList.getSignTimeList();
                        for (Long aLong : signTimeList1) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String format = simpleDateFormat.format(new Date(Long.parseLong(String.valueOf(aLong))));
                            Log.d("SignActivity", format);
                            List<String> strings = new ArrayList<>();
                            strings.add(format);
                            mSignCalendar.addMarks(strings, 0);
                            mSignCalendar.removeAllBgColor();
                        }
                    }
                });

        tv_section_title_title = (TextView) findViewById(R.id.tv_section_title_title);
        tv_section_title_title.setText("签到");

        mSignCalendar = (SignCalendar) findViewById(R.id.calendar_pager);
        mSignCalendar.setOnCalendarClickListener(this);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        data = simpleDateFormat.format(curDate);
        Log.e("SignActivity", data);


        sign_img = (ImageView) findViewById(R.id.sign_img);
        Glide.with(this)
                .load(PreferencesUtil.get(DailyBuyApplication.KEY_URL, ""))
                .dontTransform()
                .dontAnimate()
                .error(R.drawable.avatar_default)
                .placeholder(R.drawable.avatar_default)
                .into(sign_img);

//        mViewPager = (ViewPager) findViewById(R.id.calendar_pager);
//        mViewPager.setAdapter(new CalendarAdapter());
//        mViewPager.setCurrentItem(Integer.MAX_VALUE / 2);
    }

    @Override
    public void onCalendarClick(int row, int col, String dateFormat) {
        if (dateFormat.equals(data)) {
            List<String> strings = new ArrayList<>();
            strings.add(dateFormat);
            mSignCalendar.addMarks(strings, 0);
            mSignCalendar.removeAllBgColor();
            initNetwork(dateFormat);
        }
    }

    private void initNetwork(String dateFormat) {
        OkHttpUtils.get()
                .url(DailyBuyApplication.IP_URL + "signForDay.jspa")
                .addParams("userId", userId)
                .addParams("signtime", dateFormat)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("SignActivity", response);
                    }
                });

    }

//    MonthChangeListenr mMonthChangeListenr = new MonthChangeListenr() {
//
//        @Override
//        public void LastMonthClicked() {
//            mViewPager.arrowScroll(View.FOCUS_LEFT);
//        }
//
//        @Override
//        public void nextMonthClicker() {
//            mViewPager.arrowScroll(View.FOCUS_RIGHT);
//
//        }
//
//    };

//    class CalendarAdapter extends PagerAdapter {
//        CalendarSelecterView[] views = new CalendarSelecterView[5];
//        private Calendar mCalendar;
//
//        public CalendarAdapter() {
//            for (int i = 0; i < 5; i++) {
//                views[i] = new CalendarSelecterView(getBaseContext());
//                views[i].setOnMonthChangeListenr(mMonthChangeListenr);
//            }
//            mCalendar = Calendar.getInstance();
//        }

//        @Override
//        public int getItemPosition(Object object) {
//            return PagerAdapter.POSITION_NONE;
//        }
//
//        @Override
//        public int getCount() {
//            return Integer.MAX_VALUE;
//        }
//
//        @Override
//        public boolean isViewFromObject(View arg0, Object arg1) {
//            return arg0 == arg1;
//        }
//
//        @Override
//        public void destroyItem(View collection, int position, Object view) {
//            ((ViewPager) collection).removeView((View) view);
//        }
//
//        @Override
//        public Object instantiateItem(ViewGroup container, int position) {
//            CalendarSelecterView v = views[position % 5];
//            int months = Integer.MAX_VALUE / 2 - position;
//            Calendar calendar = (Calendar) mCalendar.clone();
//            calendar.add(Calendar.MONTH, -(months));
//            int year = calendar.get(Calendar.YEAR);
//            int month = calendar.get(Calendar.MONTH);
//            int day = calendar.get(Calendar.DAY_OF_MONTH);
//            Log.e("CalendarAdapter", "day:" + day);
//            v.setTime(year, month, day, false);
//            container.addView(v);
//            return v;
//        }
//    }
}
