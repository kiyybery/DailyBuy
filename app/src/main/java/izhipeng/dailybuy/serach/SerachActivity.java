package izhipeng.dailybuy.serach;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
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
import izhipeng.dailybuy.adapter.SerachHisAdapter;
import izhipeng.dailybuy.adapter.SerachHotAdapter;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.widget.CustomGridView;
import izhipeng.dailybuy.widget.ListViewForScrollView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/22 0022.
 */
public class SerachActivity extends BaseActivity {

    private CustomGridView mGridView;
    private ListViewForScrollView mListView;
    private RelativeLayout cencel_layout;
    private List<String> mList = new ArrayList<>();
    private SerachHotAdapter mHotAdapter;
    private SerachHisAdapter mHisAdapter;
    private List<String> mHotList = new ArrayList();
    private EditText serach_et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_serach);
        mGridView = (CustomGridView) findViewById(R.id.hot_serach_grid);
        mListView = (ListViewForScrollView) findViewById(R.id.his_serach_list);
        serach_et = (EditText) findViewById(R.id.serach_et);

        mHisAdapter = new SerachHisAdapter(this, mList);
        mListView.setAdapter(mHisAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("keywords", mList.get(i));
                intent.setClass(SerachActivity.this, SerachContentActivity.class);
                startActivity(intent);
            }
        });

        mHotAdapter = new SerachHotAdapter(this, mHotList);
        mGridView.setAdapter(mHotAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent();
                intent.putExtra("keywords", mHotList.get(i));
                intent.setClass(SerachActivity.this, SerachContentActivity.class);
                startActivity(intent);
            }
        });
        cencel_layout = (RelativeLayout) findViewById(R.id.cencel_layout);
        cencel_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getHotList();
        getHisList();

        serach_et.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (KeyEvent.KEYCODE_ENTER == i && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {

                    Intent intent = new Intent();
                    intent.putExtra("keywords", serach_et.getText().toString());
                    intent.setClass(SerachActivity.this, SerachContentActivity.class);
                    startActivity(intent);

                    String text = serach_et.getText().toString();
                    return true;
                }
                return false;
            }
        });
    }

    private void getHisList() {
        startProgressBar("加载中...", new Thread(), false);
        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "getHistorySearch.jspa")
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
                                showToast(jsonObject.getString("info") + "", 1000);
                            } else {
                                closeProgressBar();
                                JSONArray jsonArray = jsonObject.getJSONArray("categoryList");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    mList.add(jsonArray.optString(a));
                                }
                            }
                            mHisAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void getHotList() {
        startProgressBar("加载中...", new Thread(), false);
        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "getHotSearchs.jspa")
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
                                showToast(jsonObject.getString("info") + "", 1000);
                            } else {
                                closeProgressBar();
                                JSONArray jsonArray = jsonObject.getJSONArray("categoryList");
                                for (int a = 0; a < jsonArray.length(); a++) {
                                    mHotList.add(jsonArray.optString(a));
                                    Log.i("hotlistsize", mHotList.size() + "");
                                }
                            }
                            mHotAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
