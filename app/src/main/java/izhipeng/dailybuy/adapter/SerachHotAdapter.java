package izhipeng.dailybuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import izhipeng.dailybuy.R;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class SerachHotAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<String> mList;

    public SerachHotAdapter(Context context, List<String> mList) {
        this.mInflater = LayoutInflater.from(context);
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.adapter_serach_hot, null);
            viewHolder.textView = (TextView) view.findViewById(R.id.hot_tv);
            view.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView.setText(mList.get(i).replaceAll("\"", "") + "");
        //viewHolder.textView.setText(mList.get(i));
        return view;
    }

    class ViewHolder {
        TextView textView;
    }
}
