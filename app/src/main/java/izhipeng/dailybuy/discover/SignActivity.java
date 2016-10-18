package izhipeng.dailybuy.discover;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.SignTimeList;
import izhipeng.dailybuy.calendarview.SignCalendar;
import izhipeng.dailybuy.flake.FlakeView;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.share.ShareActivity;
import okhttp3.Call;


/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class SignActivity extends BaseActivity implements SignCalendar.OnCalendarClickListener, SignCalendar.OnCalendarDateChangedListener {

    private static final int IS_TODAY_SIGN = 2;
    //    private ViewPager mViewPager;
    //金币掉落动画的主体动画
    private FlakeView flakeView;

    private LinearLayout ll_section_title_back, ll_section_title_right;
    private TextView tv_section_title_title, sign_tag, tv_section_title_right, data_tv;
    private ImageView sign_img;
    private SignCalendar mSignCalendar;
    private String data;
    private String userId;
    private ArrayList<String> strings;
    private final static int SIGN_TAP_0 = 0;
    private final static int SIGN_TAP_1 = 1;
    private final static int SIGN_TAP_2 = 2;

    private Handler mHandler;
    private boolean isTodaySign;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign);
        flakeView = new FlakeView(this);
        userId = PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, "");
        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ll_section_title_right = (LinearLayout) findViewById(R.id.ll_section_title_right);
        ll_section_title_right.setVisibility(View.VISIBLE);
        tv_section_title_right = (TextView) findViewById(R.id.tv_section_title_right);
        tv_section_title_right.setText("分享");
        tv_section_title_right.setTextSize(14);
        tv_section_title_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("shareTitle", "来自佰惠的分享");
                intent.putExtra("shareDesp", "佰惠签到活动分享");
                intent.putExtra("shareImageUrl", "http://image.baidu.com" +
                        "arch/detail?ct=503316480&z=0&ipn=d&word=niao&hs=0&pn=1&spn=0&di=172300295090" +
                        "&pi=&rn=1&tn=baiduimagedetail&ie=utf-8&oe=utf-8&cl=2&lm=-1&cs=3013605028%2C653461985" +
                        "&os=3660383982%2C2143737810&simid=3587153136%2C709773380&adpicid=0&ln=30&fr=ala&fm=" +
                        "&sme=&cg=&bdtype=0&oriquery=niao&objurl=http%3A%2F%2Fs9.sinaimg.cn%2Fmiddle%2F485b6addtc2a2dbbfed38%26690" +
                        "&fromurl=ippr_z2C%24qAzdH3FAzdH3Fks52_z%26e3" +
                        "Bftgw_z%26e3Bv54_z%26e3BvgAzdH3FfAzdH3Fks52_9bckmw11a8a8c9m8_z%26e3Bip4s&gsm=0");
                intent.putExtra("shareWebUrl", "www.baidu.com");
                intent.setClass(SignActivity.this, ShareActivity.class);
                startActivity(intent);
            }
        });


        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        data = simpleDateFormat.format(curDate);
//        Log.e("SignActivity", data);

        data_tv = (TextView) findViewById(R.id.data_tv);
        data_tv.setText(data);

       /* simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        data = simpleDateFormat.format(curDate);
//        Log.e("SignActivity", data);*/

        tv_section_title_title = (TextView) findViewById(R.id.tv_section_title_title);
        sign_tag = (TextView) findViewById(R.id.sign_tag);
        tv_section_title_title.setText("签到");

        mSignCalendar = (SignCalendar) findViewById(R.id.calendar_pager);
        mSignCalendar.setOnCalendarClickListener(this);
        mSignCalendar.setOnCalendarDateChangedListener(this);

        initHandler();

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
                        Map<String, Integer> integerMap = new HashMap<String, Integer>();
                        String start = "2016-01-01";
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

                        String end = simpleDateFormat.format(new Date());
                        try {
                            Date dBegin = simpleDateFormat.parse(start);
                            Date dEnd = simpleDateFormat.parse(end);
                            List<Date> datesBetweenTwoDate = getDatesBetweenTwoDate(dBegin, dEnd);
                            int isEnd = 0;
                            for (Date date : datesBetweenTwoDate) {
                                isEnd++;
                                String format = simpleDateFormat.format(date);
//                                integerMap.put(format, R.drawable.icon_home_publish);
                                if (isEnd == datesBetweenTwoDate.size()) {
                                    integerMap.put(format, SIGN_TAP_2);
                                } else {
                                    integerMap.put(format, SIGN_TAP_1);
                                }
                            }
//                            Log.e("SignActivity", "integerMap.get" + integerMap.get("2016-01-01"));
                            Gson gson = new Gson();
                            SignTimeList signTimeList = gson.fromJson(response, SignTimeList.class);
                            List<Long> signTimeListLong = signTimeList.getSignTimeList();
                            for (Long aLong : signTimeListLong) {
                                String format = simpleDateFormat.format(new Date(Long.parseLong(String.valueOf(aLong))));
                                strings.add(format);
//                                integerMap.put(format, R.drawable.calendar_bg_tag);
                                integerMap.put(format, SIGN_TAP_0);
                            }

                            String today = simpleDateFormat.format(datesBetweenTwoDate.get(datesBetweenTwoDate.size() - 1));
                            int integer = integerMap.get(today);

//                            strings.clear();
//                            strings.add("2016-09-22");
//                            strings.add("2016-09-23");
//                            strings.add("2016-09-24");
//                            for (String string : strings) {
//                                integerMap.put(string, 0);
//                            }
                            mSignCalendar.addMarks(datesBetweenTwoDate, integerMap);
                            mSignCalendar.removeAllBgColor();
                            if (!(integer == SIGN_TAP_0)) {
                                List<String> strings = new ArrayList<>();
                                strings.add(today);
                                mSignCalendar.addMarks(strings, SIGN_TAP_0);
                                mSignCalendar.removeAllBgColor();
                                initNetwork(today);
                            }
                            initDateList();

//                            测试金币使用
//                            mHandler.sendEmptyMessageDelayed(0,1000);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
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

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        showPopWindows(ll_section_title_back, (int[]) msg.obj, false);
                        break;
                    case IS_TODAY_SIGN:
                        strings.add(data);
                        int i = initDateList();
                        if (isTodaySign) {
                            Message message = new Message();
                            int[] days = new int[2];
                            days[0] = i;
                            if (i != 0 && i % 7 == 0) {
                                days[1] = 50;
                            } else {
                                days[1] = 5;
                            }
                            message.what = 0;
                            message.obj = days;
                            mHandler.sendMessageDelayed(message, 500);

                        }
                        break;
                }

                return false;
            }
        });
    }

    /**
     * 根据开始时间和结束时间返回时间段内的时间集合
     *
     * @param beginDate
     * @param endDate
     * @return List
     */
    public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
        List<Date> lDate = new ArrayList<Date>();
        lDate.add(beginDate);// 把开始时间加入集合
        Calendar cal = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        cal.setTime(beginDate);
        boolean bContinue = true;
        while (bContinue) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            cal.add(Calendar.DAY_OF_MONTH, 1);
            // 测试此日期是否在指定日期之后
            if (endDate.after(cal.getTime())) {
                lDate.add(cal.getTime());
            } else {
                break;
            }
        }
        lDate.add(endDate);// 把结束时间加入集合
        return lDate;
    }


    private PopupWindow pop;

    private PopupWindow showPopWindows(View v, int[] moneyStr, boolean show) {
        View view = this.getLayoutInflater().inflate(R.layout.view_login_reward, null);
        TextView tvTips = (TextView) view.findViewById(R.id.tv_tip);
        TextView money = (TextView) view.findViewById(R.id.tv_money);
        tvTips.setText("连续登陆" + moneyStr[0] + "天，送您" + moneyStr[1] + "个爱心币");
        money.setText(moneyStr[1] + "");
        final LinearLayout container = (LinearLayout) view.findViewById(R.id.container);
        //设置同时出现在屏幕上的金币数量  建议64以内 过多会引起卡顿
//        flakeView.addFlakes(8);
        //将flakeView 添加到布局中
        container.addView(flakeView);

        //设置背景
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));

        /**
         * 绘制的类型
         * @see View.LAYER_TYPE_HARDWARE
         * @see View.LAYER_TYPE_SOFTWARE
         * @see View.LAYER_TYPE_NONE
         */
        flakeView.setLayerType(View.LAYER_TYPE_NONE, null);
        view.findViewById(R.id.btn_ikow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (container != null) {
                    container.removeAllViews();
                }
                pop.dismiss();
            }
        });
        pop = new PopupWindow(view, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (container != null) {
                    container.removeAllViews();
                }
            }
        });
        ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.half_color));
        pop.setBackgroundDrawable(dw);
        pop.setOutsideTouchable(true);
        pop.setFocusable(true);
        pop.showAtLocation(v, Gravity.CENTER, 0, 0);

        /**
         * 移除动画
         */
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //设置2秒后
                    Thread.sleep(2000);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            container.removeAllViews();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        if (!show)
            thread.start();
        return pop;
    }

    private int initDateList() {
        if (strings != null) {
            int addDay = 0;
            for (int i = 0; i < strings.size(); i++) {
                if (i == 0 && strings.size() == 1) {
                    Log.e("SignActivity", "只有一条记录" + strings.size());
                    int i1 = daysBetween(strings.get(i), data);
                    if (i1 == 1) {
                        addDay = 1;
//                        Log.d("SignActivity", "只有一个数据昨天已签到");
                    } else if (i1 > 1) {
//                        Log.d("SignActivity", "签到已中断重新计算...");
                        addDay = 0;
                    } else {
                        if (addDay == 0)
                            addDay = 1;
//                        Log.d("SignActivity", "累计签到一天");
                    }
                    sign_tag.setText("当前累计签到" + addDay + "天，还需" + (7 - (addDay % 7)) + "天就可领取50积分");
                    return addDay;
                } else {
                    if ((i + 1) == strings.size()) {
                        int lastDay = daysBetween(strings.get(i), data);
                        if (lastDay == 1) {
                            if (addDay == 0)
                                addDay = 1;
//                            Log.d("SignActivity", "昨天已签到");

                        } else if (lastDay > 1) {
//                            Log.d("SignActivity", "前天签到 已中断 重新计算");
                            addDay = 0;
                        } else {
                            if (addDay == 0)
                                addDay = 1;
//                            Log.d("SignActivity", "今天已签到");

                        }
//                        Log.d("SignActivity", "addDay:" + addDay);
                        sign_tag.setText("当前累计签到" + addDay + "天，还需" + (7 - (addDay % 7)) + "天就可领取50积分");
                        return addDay;
                    }
                    int i1 = daysBetween(strings.get(i), strings.get(i + 1));
                    if (i1 == 1) {
                        if (addDay == 0) {
                            addDay = 1;
                        }
//                        Log.d("SignActivity", "添加");
                        addDay++;
                    } else if (i1 > 1) {
                        addDay = 0;
//                        Log.d("SignActivity", "签到已中断重新计算");
                    }
                }

            }
        }
        return 0;
    }

    @Override
    public void onCalendarClick(int row, int col, String dateFormat) {
//        if (dateFormat.equals(data)) {
//            List<String> strings = new ArrayList<>();
//            strings.add(dateFormat);
//            mSignCalendar.addMarks(strings, SIGN_TAP_0);
//            mSignCalendar.removeAllBgColor();
//            initNetwork(dateFormat);
//        }
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
//                        mHandler.sendEmptyMessageDelayed(0, 1000);
//                        mHandler.sendEmptyMessageDelayed(0, 1000);
                        isTodaySign = true;
                        mHandler.sendEmptyMessage(IS_TODAY_SIGN);
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
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

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
            e.printStackTrace();
        }
        return daysBetween;
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


    @Override
    protected void onPause() {
        super.onPause();
        flakeView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        flakeView.resume();
    }
}
