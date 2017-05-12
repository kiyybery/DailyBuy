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
import izhipeng.dailybuy.bean.MyGift;


/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class MyGiftAdapter extends BaseAdapter {

    private List<MyGift> mList;
    private LayoutInflater mInflater;
    private Context mContext;

    public MyGiftAdapter(Context context, List<MyGift> list) {

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
            viewHolder.imageView = (ImageView) view.findViewById(R.id.gift_img);
            viewHolder.tv_order_id = (TextView) view.findViewById(R.id.exchangeNum);
            viewHolder.tv_push_id = (TextView) view.findViewById(R.id.push_status);
            viewHolder.tv_name_id = (TextView) view.findViewById(R.id.gift_name);
            viewHolder.tv_money_id = (TextView) view.findViewById(R.id.gift_money);
            viewHolder.tv_num_id = (TextView) view.findViewById(R.id.gift_num);
            view.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) view.getTag();
        }

        Glide.with(mContext)
                .load(mList.get(i).giftImage)
                .placeholder(R.drawable.kuangge)
                .error(R.drawable.kuangge)
                .into(viewHolder.imageView);

        viewHolder.tv_order_id.setText(mList.get(i).expressNumber);

        if (mList.get(i).tradingStatus == 1) {

            viewHolder.tv_push_id.setText("待发货");
        } else if (mList.get(i).tradingStatus == 2) {

            viewHolder.tv_push_id.setText("发货中");
        } else if (mList.get(i).tradingStatus == 3) {

            viewHolder.tv_push_id.setText("交易成功");

        }

        viewHolder.tv_name_id.setText(mList.get(i).giftName);

        viewHolder.tv_money_id.setText(mList.get(i).price);

        viewHolder.tv_num_id.setText(mList.get(i).number + "");
        return view;
    }

    class ViewHolder {

        ImageView imageView;
        TextView tv_order_id, tv_push_id, tv_name_id, tv_money_id, tv_num_id;
    }
}
