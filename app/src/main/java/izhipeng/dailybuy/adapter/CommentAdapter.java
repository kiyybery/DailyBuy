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
import izhipeng.dailybuy.bean.CommentBean;


/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class CommentAdapter extends BaseAdapter {

    private List<CommentBean> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public CommentAdapter(Context context, List<CommentBean> list) {

        this.mInflater = LayoutInflater.from(context);
        this.mList = list;
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
            view = mInflater.inflate(R.layout.adapter_comment, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.comment_img);
            viewHolder.tv_name = (TextView) view.findViewById(R.id.tv_title);
            viewHolder.tv_content = (TextView) view.findViewById(R.id.tv_content);
            viewHolder.tv_time = (TextView) view.findViewById(R.id.tv_time);
            view.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(mContext)
                .load(mList.get(i).portraitPath)
                .error(R.drawable.avatar_default)
                .placeholder(R.drawable.avatar_default)
                .dontAnimate()
                .dontTransform()
                .into(viewHolder.imageView);

        viewHolder.tv_name.setText(mList.get(i).commentName);
        viewHolder.tv_content.setText(mList.get(i).commentContent);
        viewHolder.tv_time.setText(mList.get(i).commentTime);

        return view;
    }

    class ViewHolder {

        ImageView imageView;
        TextView tv_name, tv_content, tv_time;
    }
}
