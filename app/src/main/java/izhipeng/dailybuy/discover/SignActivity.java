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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class SignActivity extends BaseActivity implements SignCalendar.OnCalendarClickListener, SignCalendar.OnCalendarDateChangedListener {

    //    private ViewPager mViewPager;
    private LinearLayout ll_section_title_back;
    private TextView tv_section_title_title, sign_tag;
    private ImageView sign_img;
    private SignCalendar mSignCalendar;
    private String data;
    private String userId;
    private ArrayList<String> strings;

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

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        data = simpleDateFormat.format(curDate);
        Log.e("SignActivity", data);

        tv_section_title_title = (TextView) findViewById(R.id.tv_section_title_title);
        sign_tag = (TextView) findViewById(R.id.sign_tag);
        tv_section_title_title.setText("签到");

        mSignCalendar = (SignCalendar) findViewById(R.id.calendar_pager);
        mSignCalendar.setOnCalendarClickListener(this);
        mSignCalendar.setOnCalendarDateChangedListener(this);

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
                        strings = new ArrayList<String>();
                        Log.e("SignActivity", response);
                        Gson gson = new Gson();
                        SignTimeList signTimeList = gson.fromJson(response, SignTimeList.class);
                        List<Long> signTimeList1 = signTimeList.getSignTimeList();
                        for (Long aLong : signTimeList1) {
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String format = simpleDateFormat.format(new Date(Long.parseLong(String.valueOf(aLong))));
                            Log.d("SignActivity", format);
                            strings.add(format);
                            mSignCalendar.addMarks(strings, 0);
                            mSignCalendar.removeAllBgColor();
                        }
                        initDateList();
                    }
                });


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

    private void initDateList() {
        if (strings != null) {
            int addDay = 0;
            for (int i = 0; i < strings.size(); i++) {
                if (i == 0 && strings.size() == 1) {
                    Log.e("SignActivity", "只有一条记录" + strings.size());
                    int i1 = daysBetween(strings.get(i), data);
                    if (i1 == 1) {
                        addDay = 1;
                        Log.d("SignActivity", "只有一个数据昨天已签到");
                    } else if (i1 > 1) {
                        Log.d("SignActivity", "签到已中断重新计算");
                        addDay = 0;
                    } else {
                        if (addDay == 0)
                            addDay = 1;
                        Log.d("SignActivity", "累计签到一天");
                    }
                    break;
                }
                if ((i + 1) == strings.size()) {
                    int lastDay = daysBetween(strings.get(i), data);
                    if (lastDay == 1) {
                        if (addDay == 0)
                            addDay = 1;
                        Log.d("SignActivity", "昨天已签到");
                    } else if (lastDay > 1) {
                        Log.d("SignActivity", "前天签到 已中断 重新计算");
                        addDay = 0;
                    }
                    break;
                }
                int i1 = daysBetween(strings.get(i), strings.get(i + 1));
                if (i1 == 1) {
                    if (addDay == 0) {
                        addDay = 1;
                    }
                    Log.d("SignActivity", "添加");
                    addDay++;
                } else if (i1 > 1) {
                    addDay = 0;
                    Log.d("SignActivity", "签到已中断重新计算");
                }
            }
            Log.d("SignActivity", "addDay:" + addDay);
            sign_tag.setText("当前累计签到" + addDay + "天，还需" + (7 - (addDay % 7)) + "天就可领取50积分");
        }
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


    /**
     * 计算两天之间的天数
     *
     * @param startStr
     * @param endStr
     * @return
     */
    public static int daysBetween(String startStr, String endStr) {
        int daysBetween = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date date1 = sdf.parse(startStr);
            Calendar startDate = Calendar.getInstance();
            startDate.setTime(date1);

            Date date2 = sdf.parse(endStr);
            Calendar endDate = Calendar.getInstance();
            endDate.setTime(date2);

            Calendar date = (Calendar) startDate.clone();

            while (date.before(endDate)) {
                date.add(Calendar.DAY_OF_MONTH, 1);
                daysBetween++;
            }

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return daysBetween;
    }

    /**
     * 计算连续签到
     * 1.签到之后才开始调用该方法，所以l中的最新日期为当天
     * 2.参数l中的日期必须是按签到日期降序排列
     * 3.参数l中的日期格式是java.sql.Date或满足yyyy-MM-dd的，不带小时分钟秒
     */
    public static int calRowDate(List<Date> l) {
        if (l == null || l.size() == 0) return 0;
        int num = 1;
        int size = 0;
        //判断最新的签到是不是当天，如不是，返回0
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date cd = l.get(0);//取l中最新日期
        if (!(sdf.format(date).equals(sdf.format(cd)))) {
            return 0;
        }
        if (l.size() == 0) {
            return 1;
        }
        long l1, l2;
        while (size + 1 < l.size()) {
            l1 = l.get(size).getTime();
            l2 = l.get(size + 1).getTime();
            if (l1 - l2 == 1000 * 60 * 60 * 24) {
                num++;
                size = size + 1;
            } else {
                break;
            }
        }
        return num;
    }

    @Override
    public void onCalendarDateChanged(int year, int month) {

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
