package izhipeng.dailybuy.calendarview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;
import okhttp3.Call;

/**
 * Adapter that show days in one month
 *
 * @author Liyanshun
 */
public class CalendarGridAdapter extends BaseAdapter {
    private Calendar mCalendar;
    private ArrayList<Integer> mDaysArrayList = new ArrayList<Integer>();
    private ArrayList<Integer> mDaysTypeList = new ArrayList<Integer>();
    private Context mContext;
    private final static int DAYS_OF_LAST_MONTHS = 0;
    private final static int DAYS_OF_NEXT_MONTHS = 1;
    private final static int DAYS_OF_CURRENT_MONTH = 2;
    private int mYear;
    private int mMonth;
    private int mDay;
    private boolean mshowOtherMonth;
    private int mSelectedPosition = -1;

    public void setCheckedPosition(int checkedPosition) {
        switch (getItemViewType(checkedPosition)) {
            case DAYS_OF_CURRENT_MONTH:
                mSelectedPosition = checkedPosition;
                notifyDataSetChanged();
                break;
            default:
                break;
        }

    }

    public CalendarGridAdapter(Context c) {
        mContext = c;
    }

    public void setTime(int year, int month, int day, boolean showOtherMonth) {
        mSelectedPosition = -1;
        mDaysArrayList.clear();
        mDaysTypeList.clear();
        mYear = year;
        mMonth = month;
        mDay = day;
        mshowOtherMonth = showOtherMonth;
        mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int daySpacing = getDaySpacing(mCalendar.get(Calendar.DAY_OF_WEEK));
        if (daySpacing > 0) {
            Calendar lastMonth = (Calendar) mCalendar.clone();
            lastMonth.add(Calendar.MONTH, -1);
            lastMonth.set(Calendar.DAY_OF_MONTH,
                    lastMonth.getActualMaximum(Calendar.DAY_OF_MONTH)
                            - daySpacing + 1);
            for (int i = 0; i < daySpacing; i++) {
                mDaysArrayList.add(lastMonth.get(Calendar.DAY_OF_MONTH));
                mDaysTypeList.add(DAYS_OF_LAST_MONTHS);
                lastMonth.add(Calendar.DAY_OF_MONTH, 1);
            }
        }

        int daysOfThisMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= daysOfThisMonth; i++) {
            mDaysArrayList.add(i);
            mDaysTypeList.add(DAYS_OF_CURRENT_MONTH);
        }

        mCalendar.set(Calendar.DAY_OF_MONTH,
                mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        daySpacing = getDaySpacingEnd(mCalendar.get(Calendar.DAY_OF_WEEK));

        if (daySpacing > 0) {
            Calendar nextMonth = (Calendar) mCalendar.clone();
            nextMonth.add(Calendar.MONTH, 1);
            nextMonth.set(Calendar.DAY_OF_MONTH, 1);
            for (int i = 0; i < daySpacing; i++) {
                mDaysArrayList.add(nextMonth.get(Calendar.DAY_OF_MONTH));
                mDaysTypeList.add(DAYS_OF_NEXT_MONTHS);
                nextMonth.add(Calendar.DAY_OF_MONTH, 1);
            }
        }
        notifyDataSetChanged();

    }

    private int getDaySpacing(int dayOfWeek) {
        if (Calendar.SATURDAY == dayOfWeek)
            return 6;
        else
            return dayOfWeek - 1;
    }

    private int getDaySpacingEnd(int dayOfWeek) {
        return 7 - dayOfWeek;
    }

    @Override
    public int getCount() {
        return mDaysArrayList.size();
    }

    @Override
    public Integer getItem(int position) {
        return mDaysArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public int getItemViewType(int position) {
        return mDaysTypeList.get(position);
    }

    public int getViewTypeCount() {
        return 3;
    }

    public String getSelectedDate(int position) {
        String dateString = null;
        switch (getItemViewType(position)) {
            case DAYS_OF_CURRENT_MONTH:
                dateString = mYear + "-" + (mMonth + 1) + "-" + getItem(position);
                break;
            case DAYS_OF_NEXT_MONTHS:
                if (mshowOtherMonth) {
                    dateString = mYear + "-" + (mMonth + 2) + "-"
                            + getItem(position);
                }
                break;
            case DAYS_OF_LAST_MONTHS:
                if (mshowOtherMonth) {
                    dateString = mYear + "-" + (mMonth) + "-" + getItem(position);
                }
                break;
        }
        return dateString;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.grid_item, null);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.grid_item_text);
            holder.imageView = (ImageView) convertView
                    .findViewById(R.id.sign_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (getItemViewType(position)) {
            case DAYS_OF_CURRENT_MONTH:
                setTextStatus(position, holder);
                break;
            case DAYS_OF_NEXT_MONTHS:
            case DAYS_OF_LAST_MONTHS:
                if (mshowOtherMonth) {
                    setTextStatus(position, holder);
                } else {
                    holder.textView.setText("");
                }
                break;
            default:
                break;
        }

        return convertView;
    }

    private void setTextStatus(int position, final ViewHolder holder) {
        holder.textView.setText(String.valueOf(getItem(position)));
        if (mSelectedPosition == position) {
            holder.textView.setTextColor(mContext.getResources().getColor(
                    R.color.calendar_month_text_color));


            OkHttpUtils
                    .post()
                    .url(DailyBuyApplication.IP_URL + "signForDay.jspa")
                    .addParams("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))
                    .addParams("signtime", getSelectedDate(position))
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {

                        }

                        @Override
                        public void onResponse(String s, int i) {

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getInt("ret") == 1) {

                                    Toast.makeText(mContext, jsonObject.getString("info"),
                                            Toast.LENGTH_LONG).show();
                                    holder.textView.setSelected(true);
                                    holder.imageView.setImageResource(R.drawable.icon_sign_true);
                                } else {

                                    Toast.makeText(mContext, jsonObject.getString("info"),
                                            Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            holder.textView.setTextColor(mContext.getResources().getColor(
                    R.color.calendar_month_text_color));
            holder.textView.setSelected(false);
        }
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
