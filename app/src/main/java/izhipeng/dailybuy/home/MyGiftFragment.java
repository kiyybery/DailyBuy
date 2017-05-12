package izhipeng.dailybuy.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import izhipeng.dailybuy.BaseFragment;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.adapter.MyGiftAdapter;
import izhipeng.dailybuy.bean.MyGift;
import izhipeng.dailybuy.widget.XListView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/8 0008.
 */
public class MyGiftFragment extends BaseFragment {

    private XListView mListView;
    private List<MyGift> mList;
    private MyGift bean;
    private MyGiftAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mList = new ArrayList<>();
        bean = new MyGift();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mygift, container, false);
        startProgressBar("加载中...", new Thread(), false);
        getGiftList();

        mListView = (XListView) view.findViewById(R.id.ListView);
        mAdapter = new MyGiftAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent();

                intent.putExtra("giftImage", mList.get(i).giftImage);
                intent.putExtra("giftName", mList.get(i).giftName);
                intent.putExtra("price", mList.get(i).price);
                intent.putExtra("number", mList.get(i).number);
                intent.putExtra("createTime", mList.get(i).createTime);
                intent.putExtra("expressNumber", mList.get(i).expressNumber);
                intent.putExtra("expressage", mList.get(i).expressage);
                intent.putExtra("tradingStatus", mList.get(i).tradingStatus);

                intent.setClass(getActivity(), MyGiftDetialActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    public void getGiftList() {

        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "getMyGift.jspa")
                .addParams("userId", "XdDXmgK5i/4=")
                .addParams("pageId", 1 + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        closeProgressBar();
                        showToast(e + "", 1000);
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("ret") == 1) {

                                closeProgressBar();
                                showToast(jsonObject.getString("info"), 1000);
                                JSONArray array = jsonObject.getJSONArray("exchangeRecordVO2List");
                                for (int a = 0; a < array.length(); a++) {

                                    JSONObject object = (JSONObject) array.get(i);
                                    bean.expressNumber = object.getString("expressNumber");
                                    bean.tradingStatus = object.getInt("tradingStatus");
                                    bean.giftImage = object.getString("giftImage");
                                    bean.giftName = object.getString("giftName");
                                    bean.price = object.getString("price");
                                    bean.number = object.getInt("number");
                                    bean.expressage = object.getString("expressage");

                                    mList.add(bean);
                                }
                                mAdapter.notifyDataSetChanged();
                            } else {

                                closeProgressBar();
                                showToast(jsonObject.getString("info"), 1000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
