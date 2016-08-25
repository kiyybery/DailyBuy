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
import izhipeng.dailybuy.bean.SerachContentBean;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class SerachContentAdapter extends BaseAdapter {
    private List<SerachContentBean> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public SerachContentAdapter(Context mContext, List<SerachContentBean> mList) {

        this.mInflater = LayoutInflater.from(mContext);
        this.mList = mList;
        this.mContext = mContext;
    }

    public void resetData(List<SerachContentBean> beanList) {

        this.mList.clear();
        this.mList.addAll(beanList);
        this.notifyDataSetChanged();
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
            view = mInflater.inflate(R.layout.adapter_serach_content, null);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.serach_content_img);
            viewHolder.title_tv = (TextView) view.findViewById(R.id.title_name_tv);
            viewHolder.tag_tv = (TextView) view.findViewById(R.id.tag_tv);
            viewHolder.pnum_tv = (TextView) view.findViewById(R.id.pnum_tv);
            viewHolder.like_tv = (TextView) view.findViewById(R.id.likenum);
            viewHolder.comment_icon = (ImageView) view.findViewById(R.id.comment_icon);
            view.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        Glide
                .with(mContext)
                .load(mList.get(i).infoImg)
                .dontTransform()
                .dontAnimate()
                .placeholder(R.drawable.kuangge)
                .error(R.drawable.kuangge)
                .into(viewHolder.imageView);
        viewHolder.title_tv.setText(mList.get(i).infoTitle);
        viewHolder.tag_tv.setText(mList.get(i).label);
        viewHolder.pnum_tv.setText(mList.get(i).infoOwner);
        viewHolder.like_tv.setText(String.valueOf(mList.get(i).likeNums));
        if (mList.get(i).likeStatus == 1) {

            viewHolder.comment_icon.setImageResource(R.drawable.like_icon_press);
        }
        return view;
    }

    class ViewHolder {

        ImageView imageView, comment_icon;
        TextView title_tv, tag_tv, pnum_tv, like_tv;
    }
}
