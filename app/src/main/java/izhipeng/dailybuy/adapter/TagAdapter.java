package izhipeng.dailybuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.TagBean;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class TagAdapter extends BaseAdapter {

    private Context mContext;
    private List<TagBean> mList;
    boolean isSelect;
    private int selectedPosition = 0;

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public TagAdapter(Context mContext, List<TagBean> mlist) {
        this.mContext = mContext;
        this.mList = mlist;
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
        TagBean tagBean = (TagBean) getItem(i);
        view = LayoutInflater.from(mContext).inflate(R.layout.adapter_tag, null);
        TextView textView = (TextView) view.findViewById(R.id.tag_tv);
        textView.setText(tagBean.getTag());

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
