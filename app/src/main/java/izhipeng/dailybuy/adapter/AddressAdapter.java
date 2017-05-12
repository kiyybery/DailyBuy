package izhipeng.dailybuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.AddressBean;

/**
 * Created by Administrator on 2016/10/27 0027.
 */
public class AddressAdapter extends BaseAdapter {

    private List<AddressBean> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    public AddressAdapter(List<AddressBean> list, Context context) {

        this.mList = list;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
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
            view = mInflater.inflate(R.layout.address_adapter, null);
            viewHolder.checkBox = (CheckBox) view.findViewById(R.id.checkbox_id);
            viewHolder.textView1 = (TextView) view.findViewById(R.id.address_tv);
            viewHolder.textView2 = (TextView) view.findViewById(R.id.address_name_tv);
            viewHolder.textView3 = (TextView) view.findViewById(R.id.address_pNum);
            view.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.textView1.setText(mList.get(i).address);
        viewHolder.textView2.setText(mList.get(i).name);
        viewHolder.textView3.setText(mList.get(i).pNum);

        return view;
    }

    class ViewHolder {

        CheckBox checkBox;
        TextView textView1, textView2, textView3;
    }
}
