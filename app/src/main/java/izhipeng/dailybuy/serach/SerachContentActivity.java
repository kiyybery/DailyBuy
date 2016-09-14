package izhipeng.dailybuy.serach;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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
import izhipeng.dailybuy.adapter.SerachContentAdapter;
import izhipeng.dailybuy.bean.SerachContentBean;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.widget.XListView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class SerachContentActivity extends BaseActivity implements XListView.IXListViewListener {

    private List<SerachContentBean> mList = new ArrayList<>();
    private XListView mListView;
    private SerachContentAdapter mAdapter;
    private String keyWords;
    private EditText serach_et;
    private RelativeLayout cencel_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serach_content);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        keyWords = getIntent().getStringExtra("keywords");
        startProgressBar("加载中...", new Thread(), false);
        getSerachContentList(keyWords);
        mListView = (XListView) findViewById(R.id.mListView);
        mAdapter = new SerachContentAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setPullLoadEnable(false);
        mListView.setXListViewListener(this);
        mListView.setPullRefreshEnable(false);

        serach_et = (EditText) findViewById(R.id.serach_et);
        serach_et.setText(keyWords);
        serach_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                if (KeyEvent.KEYCODE_ENTER == i && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    getSerachContentList(serach_et.getText().toString());
                    mAdapter.resetData(mList);
                    return true;
                }
                return false;
            }
        });
        cencel_layout = (RelativeLayout) findViewById(R.id.cencel_layout);
        cencel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void getSerachContentList(String keywords) {

        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "searchFavorableList.jspa")
                .addParams("keyWords", keywords)
                .addParams("pageId", 1 + "")
                .addParams("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))
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
                                JSONArray array = jsonObject.getJSONArray("favorableVOList");
                                if (array.length() == 0) {

                                    showToast("搜索结果为空！", 1000);
                                }
                                for (int a = 0; a < array.length(); a++) {
                                    showToast("搜索成功！", 1000);
                                    JSONObject object = (JSONObject) array.get(a);
                                    SerachContentBean bean = new SerachContentBean();
                                    bean.infoId = object.getInt("infoId");
                                    bean.infoTitle = object.getString("infoTitle");
                                    bean.infoImg = object.getString("infoImg");
                                    bean.infoOwner = object.getString("infoOwner");
                                    bean.likeNums = object.getInt("likeNums");
                                    bean.label = object.getString("label");
                                    bean.likeStatus = object.getInt("likeStatus");
                                    bean.webUrl = object.getString("webUrl");

                                    mList.add(bean);
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
