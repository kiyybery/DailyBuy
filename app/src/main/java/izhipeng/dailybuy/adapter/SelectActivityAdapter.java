package izhipeng.dailybuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.SelectLoginBean;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class SelectActivityAdapter extends BaseAdapter {

    private Context mContext;
    private List<SelectLoginBean> mList;
    boolean isSelect;
    private int selectedPosition = 0;

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public SelectActivityAdapter(Context mContext, List<SelectLoginBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
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
        SelectLoginBean bean = (SelectLoginBean) getItem(i);
        view = LayoutInflater.from(mContext).inflate(R.layout.item_select_login, null);
        final TextView textView = (TextView) view.findViewById(R.id.name_tv);
        textView.setText(bean.getText());

        if (i == selectedPosition) {

            textView.setSelected(true);
            isSelect = true;
        } else {

            textView.setSelected(false);
            isSelect = false;
        }
        return view;
    }
}
