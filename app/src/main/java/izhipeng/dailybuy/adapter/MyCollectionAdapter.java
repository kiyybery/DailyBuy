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
import izhipeng.dailybuy.bean.MyCollecContent;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class MyCollectionAdapter extends BaseAdapter {

    private List<MyCollecContent> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MyCollectionAdapter(Context mContext, List<MyCollecContent> mList) {
        this.mList = mList;
        this.mInflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
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
            view = mInflater.inflate(R.layout.adapter_mycollec, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.img);
            viewHolder.tv_title = (TextView) view.findViewById(R.id.title_tv);
            viewHolder.tv_comment = (TextView) view.findViewById(R.id.comment_tv);
            viewHolder.tv_like_num = (TextView) view.findViewById(R.id.like_tv);
            viewHolder.imageView_publish = (ImageView) view.findViewById(R.id.store_icon);
            viewHolder.tv_store = (TextView) view.findViewById(R.id.store_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(mContext)
                .load(mList.get(i).favorImage)
                .dontTransform()
                .error(R.drawable.kuangge)
                .into(viewHolder.imageView);

        viewHolder.tv_title.setText(mList.get(i).favorTitle);
        viewHolder.tv_comment.setText(String.valueOf(mList.get(i).commentNums));
        viewHolder.tv_like_num.setText(String.valueOf(mList.get(i).likeNums));
        Glide.with(mContext)
                .load(mList.get(i).publishPortrait)
                .dontTransform()
                .error(R.drawable.kuangge)
                .into(viewHolder.imageView_publish);
        viewHolder.tv_store.setText(mList.get(i).publishName);
        return view;
    }

    class ViewHolder {

        ImageView imageView, imageView_publish;
        TextView tv_title, tv_comment, tv_like_num, tv_store;
    }
}
