package izhipeng.dailybuy.myprefire;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
import izhipeng.dailybuy.adapter.MyCollectionAdapter;
import izhipeng.dailybuy.bean.MyCollecContent;
import izhipeng.dailybuy.home.HomeDetialActivity;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.widget.XListView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/16 0016.
 */
public class MyCollecContentFragment extends BaseFragment implements XListView.IXListViewListener {

    private XListView mListView;
    private List<MyCollecContent> mList = new ArrayList();
    private MyCollecContent mBean;
    private MyCollectionAdapter mAdapter;

    public static MyCollecContentFragment newInstance() {

        MyCollecContentFragment fragment = new MyCollecContentFragment();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mycolleccontent, container, false);

        startProgressBar("加载中...", new Thread(), false);
        getContent();
        mAdapter = new MyCollectionAdapter(getActivity(), mList);
        mListView = (XListView) view.findViewById(R.id.mListview);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("webUrl", mList.get(i - 1).webUrl);
                intent.putExtra("state", mList.get(i - 1).state);
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
                .url(DailyBuyApplication.IP_URL + "favorContentListByUid.jspa")
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
                            Log.i("111_log", jsonObject + "");
                            if (jsonObject.getInt("ret") == 2) {
                                closeProgressBar();
                                showToast(jsonObject.getString("info"), 1000);
                            } else {
                                closeProgressBar();
                                JSONArray array = jsonObject.getJSONArray("favorContentVOList");
                                for (int a = 0; a < array.length(); a++) {
                                    JSONObject object = (JSONObject) array.get(a);
                                    mBean = new MyCollecContent();
                                    mBean.commentNums = object.getInt("commentNums");
                                    mBean.favorId = object.getInt("favorId");
                                    mBean.favorImage = object.getString("favorImage");
                                    mBean.favorTitle = object.getString("favorTitle");
                                    mBean.likeNums = object.getInt("likeNums");
                                    mBean.webUrl = object.getString("webUrl");
                                    mBean.publishName = object.getString("publishName");
                                    mBean.publishPortrait = object.getString("publishPortrait");
                                    mBean.state = object.getInt("state");

                                    mList.add(mBean);
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
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
