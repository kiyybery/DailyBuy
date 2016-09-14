package izhipeng.dailybuy.calendarview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;

import izhipeng.dailybuy.R;

/**
 * A view that let users select date
 *
 * @author Liyanshun
 */
public class CalendarSelecterView extends RelativeLayout implements
        OnItemClickListener {

    private OnDateSelectedListener mOnDateSelectedListener;
    private CalendarGridAdapter mDayOfMonthAdapter;
    private boolean mShowLastAndNextButton;
    private MonthChangeListenr mMonthChangeListenr;
    private View mBodyLayout;
    private TextView mCrrentMonthTextView;
    private ImageButton mLastMonthButton;
    private ImageButton mNextMonthButton;
    private GridView mDayOfWeekView;
    private GridView mDayOfMonthGridView;

    public CalendarSelecterView(Context context) {
        super(context);
        init();
    }

    public CalendarSelecterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CalendarSelecterView(Context context, AttributeSet attrs,
                                int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mBodyLayout = LayoutInflater.from(getContext()).inflate(
                R.layout.selecter_view, null, false);
        mCrrentMonthTextView = (TextView) mBodyLayout
                .findViewById(R.id.current_month);

        mDayOfWeekView = (GridView) mBodyLayout.findViewById(R.id.day_of_week);

        DayOfWeekAdapter dayOfWeekAdapter = new DayOfWeekAdapter();
        mDayOfWeekView.setAdapter(dayOfWeekAdapter);

        mDayOfMonthGridView = (CalendarGridView) mBodyLayout
                .findViewById(R.id.day_of_month);
        mDayOfMonthAdapter = new CalendarGridAdapter(getContext());
        mDayOfMonthGridView.setAdapter(mDayOfMonthAdapter);
        mDayOfMonthGridView.setOnItemClickListener(this);
        addView(mBodyLayout);
    }

    public void setOnMonthChangeListenr(MonthChangeListenr l) {
        mMonthChangeListenr = l;
        mShowLastAndNextButton = true;
    }

    public void setOnDateSelectedListener(OnDateSelectedListener l) {
        mOnDateSelectedListener = l;
    }

    public void setTime(int year, int month, int day, boolean showOtherMonth) {
        mCrrentMonthTextView.setText(year + "年" + (month + 1) + "月" + day + "日");
        /*if (mShowLastAndNextButton) {
            mLastMonthButton.setVisibility(View.VISIBLE);
            mNextMonthButton.setVisibility(View.VISIBLE);
        } else {
            mLastMonthButton.setVisibility(View.GONE);
            mNextMonthButton.setVisibility(View.GONE);
        }*/
        mDayOfMonthAdapter.setTime(year, month, day, showOtherMonth);
    }

    class DayOfWeekAdapter extends BaseAdapter {
        private final String[] DAY_IN_WEEK = new String[7];

        public DayOfWeekAdapter() {

            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            for (int i = 0; i < 7; i++) {
                DAY_IN_WEEK[i] = cal.getDisplayName(Calendar.DAY_OF_WEEK,
                        Calendar.SHORT, Locale.getDefault());
                cal.add(Calendar.DAY_OF_WEEK, 1);
            }
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int position) {
            return DAY_IN_WEEK[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = View.inflate(getContext(),
                    R.layout.day_of_week_grid_item, null);
            TextView t = (TextView) convertView
                    .findViewById(R.id.grid_item_text);
            t.setText(DAY_IN_WEEK[position]);
            //t.setTextAppearance(getContext(), R.style.DayOfWeekStyle);
            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        mDayOfMonthAdapter.setCheckedPosition(position);
        if (mOnDateSelectedListener != null) {
            mOnDateSelectedListener.OnDateSelected(mDayOfMonthAdapter
                    .getSelectedDate(position));
        }
    }
}
