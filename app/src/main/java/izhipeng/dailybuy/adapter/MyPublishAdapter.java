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
import izhipeng.dailybuy.bean.MyPublish;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class MyPublishAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<MyPublish> mList;

    public MyPublishAdapter(Context mContext, List<MyPublish> mList) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
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
            view = mInflater.inflate(R.layout.adapter_mypublish, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.info_img);
            viewHolder.title = (TextView) view.findViewById(R.id.info_title);
            viewHolder.time = (TextView) view.findViewById(R.id.info_time);
            view.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) view.getTag();
        }
        Glide.with(mContext)
                .load(mList.get(i).img)
                .dontTransform()
                .dontAnimate()
                .placeholder(R.drawable.avatar_default)
                .error(R.drawable.avatar_default)
                .into(viewHolder.imageView);
        /*viewHolder.imageView.setImageResource(R.drawable.uc_my_background);

        viewHolder.title_tv.setText(mList.get(i).getTitle());
        viewHolder.time_tv.setText(mList.get(i).getDate());*/
        viewHolder.title.setText(mList.get(i).title);
        return view;
    }

    class ViewHolder {

        ImageView imageView;
        TextView title, time;
    }
}
