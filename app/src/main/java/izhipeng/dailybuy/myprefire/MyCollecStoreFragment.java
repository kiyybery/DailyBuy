package izhipeng.dailybuy.myprefire;

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
import izhipeng.dailybuy.adapter.MyCollecStoreAdapter;
import izhipeng.dailybuy.bean.MyCollecStore;
import izhipeng.dailybuy.home.HomeDetialActivity;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.widget.XListView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/17 0017.
 */
public class MyCollecStoreFragment extends BaseFragment implements XListView.IXListViewListener {

    private List<MyCollecStore> mList = new ArrayList<>();
    private XListView mListView;
    private MyCollecStoreAdapter mAdapter;

    public static MyCollecStoreFragment newInstance() {

        MyCollecStoreFragment fragment = new MyCollecStoreFragment();
        Bundle data = new Bundle();
        data.putString("title", " ");
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mycolleccontent, container, false);
        startProgressBar("加载中...", new Thread(), false);
        getContent();
        mListView = (XListView) view.findViewById(R.id.mListview);
        mAdapter = new MyCollecStoreAdapter(mList, getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("webUrl", mList.get(i - 1).storeUrl);
                intent.setClass(getActivity(), HomeDetialActivity.class);
                startActivity(intent);
            }
        });
        mListView.setPullLoadEnable(false);
        mListView.setXListViewListener(this);
        mListView.setPullRefreshEnable(false);
        return view;
    }

    private void getContent() {

        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "favorStoreListByUserId.jspa")
                .addParams("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))
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
                            if (jsonObject.getInt("ret") == 2) {
                                closeProgressBar();
                                showToast(jsonObject.getString("info"), 1000);
                            } else {

                                closeProgressBar();
                                JSONArray array = jsonObject.getJSONArray("favorStoreContentVOList");
                                for (int a = 0; a < array.length(); a++) {

                                    JSONObject object = (JSONObject) array.get(a);
                                    MyCollecStore store = new MyCollecStore();
                                    store.favorId = object.getInt("favorId");
                                    store.storeId = object.getInt("storeId");
                                    store.storeImage = object.getString("storeImage");
                                    store.storeName = object.getString("storeName");

                                    mList.add(store);
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
