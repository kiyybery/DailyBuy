package izhipeng.dailybuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.MyPrefire;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class MyPrefireAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyPrefire> mList;
    private int selectedPosition = 0;
    boolean isSelect;

    public MyPrefireAdapter(Context context, List<MyPrefire> list) {

        this.mContext = context;
        this.mList = list;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList == null ? null : mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MyPrefire myPrefire = (MyPrefire) getItem(i);
        view = LayoutInflater.from(mContext).inflate(R.layout.item_myprefire, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.left_img);
        TextView textView = (TextView) view.findViewById(R.id.content_tv);
        final LinearLayout layout_select = (LinearLayout) view.findViewById(R.id.layout_select);
        imageView.setImageResource(myPrefire.getImg());
        textView.setText(myPrefire.getText());

        if (i == selectedPosition) {

            layout_select.setSelected(true);
            textView.setSelected(true);
            isSelect = true;
        } else {

            layout_select.setSelected(false);
            textView.setSelected(false);
            isSelect = false;
        }
        return view;
    }
}
