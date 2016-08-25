package izhipeng.dailybuy.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.MyCollecStore;

/**
 * Created by Administrator on 2016/8/17 0017.
 */
public class MyCollecStoreAdapter extends BaseAdapter {

    private List<MyCollecStore> mList;
    private Context mContext;
    private LayoutInflater mInfalter;

    public MyCollecStoreAdapter(List<MyCollecStore> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        this.mInfalter = LayoutInflater.from(mContext);
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
            view = mInfalter.inflate(R.layout.adapter_collecstore, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.stroe_img);
            viewHolder.textView = (TextView) view.findViewById(R.id.store_name);
            view.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(mContext)
                .load(mList.get(i).storeImage)
                .dontTransform()
                .error(R.drawable.kuangge)
                .into(viewHolder.imageView);
        viewHolder.textView.setText(mList.get(i).storeName);
        return view;
    }

    class ViewHolder {

        ImageView imageView;
        TextView textView;
    }
}
