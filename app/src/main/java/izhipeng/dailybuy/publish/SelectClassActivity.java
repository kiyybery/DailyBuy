package izhipeng.dailybuy.publish;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import izhipeng.dailybuy.adapter.SelectClassAdapter;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class SelectClassActivity extends BaseActivity {

    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private SelectClassAdapter mAdapter;
    private LinearLayout ll_section_title_back;
    private TextView tv_section_title_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_class);

        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_section_title_title = (TextView) findViewById(R.id.tv_section_title_title);
        tv_section_title_title.setText("分类");
        startProgressBar("加载中...", new Thread(), false);
        getContent();
        mListView = (ListView) findViewById(R.id.mListView);
        mAdapter = new SelectClassAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //showToast("click " + mList.get(i).toString(), 1000);
                Intent intent = new Intent();
                intent.putExtra("name", mList.get(i).toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void getContent() {

        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "getCategoryList.jspa")
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
                                JSONArray jsonArray = jsonObject.getJSONArray("categoryList");
                                for (int a = 0; a < jsonArray.length(); a++) {

                                    mList.add(jsonArray.optString(a));
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
    }
}
