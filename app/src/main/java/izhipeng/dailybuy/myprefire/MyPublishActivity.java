package izhipeng.dailybuy.myprefire;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.adapter.MyPublishAdapter;
import izhipeng.dailybuy.bean.MyPublish;
import izhipeng.dailybuy.widget.XListView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class MyPublishActivity extends BaseActivity implements XListView.IXListViewListener {

    private List<MyPublish> mList = new ArrayList<>();
    private XListView mListView;
    private MyPublishAdapter mAdapter;
    private LinearLayout mBack;
    private TextView mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_publish);

        mBack = (LinearLayout) findViewById(R.id.ll_section_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTitle = (TextView) findViewById(R.id.tv_section_title_title);
        mTitle.setText("我的发布");
        startProgressBar("加载中...", new Thread(), true);
        getPublishList();
        mAdapter = new MyPublishAdapter(MyPublishActivity.this, mList);
        mListView = (XListView) findViewById(R.id.mListView);
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(false);
        mListView.setXListViewListener(this);
        mListView.setPullRefreshEnable(false);
    }

    private void getPublishList() {

        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "favorableListByUserId.jspa")
                .addParams("userId", "e2os3NIaWaA=")
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
                            Log.i("111_log", s + "");
                            if (jsonObject.getInt("ret") == 0) {
                                closeProgressBar();
                                showToast(jsonObject.getString("info"), 1000);
                            } else {
                                closeProgressBar();
                                JSONArray array = jsonObject.getJSONArray("favorableList");
                                for (int a = 0; a < array.length(); a++) {
                                    JSONObject job = (JSONObject) array.get(a);
                                    MyPublish publish = new MyPublish();
                                    publish.img = job.getString("infoImg");
                                    publish.title = job.getString("infoTitle");
                                    publish.c_time = job.getString("createTimeStr");

                                    mList.add(publish);
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
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
