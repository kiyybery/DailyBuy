package izhipeng.dailybuy.publish;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.adapter.TagAdapter;
import izhipeng.dailybuy.bean.TagBean;
import izhipeng.dailybuy.library.FileUtilsOfPaul;
import izhipeng.dailybuy.library.PreferencesUtil;
import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class PublishStoreActivity extends BaseActivity {

    private LinearLayout ll_section_title_back, ll_section_title_right, tag_layout, select_layout;
    private TextView tv_section_title_title, tv_section_title_right;
    private ImageView activity_pic;
    private EditText title_ed, activity_info, activity_store_name,
            activity_time, activity_link, store_address;
    private String path;
    private GridView tag_grid;
    private TagAdapter mAdapter;
    private List<TagBean> mList = new ArrayList<>();
    String name, tag_name;

    public static final int REQUEST_MODIFY_PICTURE = 200;
    public static final int REQUEST_MODIFY_SELECT = 201;

    public String[] tag = new String[]{
            "折扣券", "返现", "领取现金", "优惠券"

    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_store);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        for (int i = 0; i < tag.length; i++) {
            TagBean bean = new TagBean();
            bean.setTag(tag[i]);
            mList.add(bean);
        }

        ll_section_title_back = (LinearLayout) findViewById(R.id.ll_section_title_back);
        ll_section_title_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_section_title_title = (TextView) findViewById(R.id.tv_section_title_title);
        tv_section_title_title.setText("活动发布");

        ll_section_title_right = (LinearLayout) findViewById(R.id.ll_section_title_right);
        ll_section_title_right.setVisibility(View.VISIBLE);

        tv_section_title_right = (TextView) findViewById(R.id.tv_section_title_right);
        tv_section_title_right.setText("发布");
        tv_section_title_right.setOnClickListener(this);
        activity_pic = (ImageView) findViewById(R.id.activity_pic);
        activity_pic.setOnClickListener(this);

        title_ed = (EditText) findViewById(R.id.activity_title);
        activity_info = (EditText) findViewById(R.id.activity_info);
        activity_store_name = (EditText) findViewById(R.id.activity_store_name);
        activity_time = (EditText) findViewById(R.id.activity_time);
        activity_link = (EditText) findViewById(R.id.activity_link);


        store_address = (EditText) findViewById(R.id.store_address);
        tag_layout = (LinearLayout) findViewById(R.id.tag_layout);
        select_layout = (LinearLayout) findViewById(R.id.select_layout);
        select_layout.setOnClickListener(this);
        tag_grid = (GridView) findViewById(R.id.tag_grid);
        mAdapter = new TagAdapter(this, mList);
        tag_grid.setAdapter(mAdapter);
        tag_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                showToast(mList.get(i).getTag(), 1000);
                tag_name = mList.get(i).getTag();
                mAdapter.setSelectedPosition(i);
                mAdapter.notifyDataSetInvalidated();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.activity_pic:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_MODIFY_PICTURE);
                break;

            case R.id.tv_section_title_right:

                pullPic();
                break;

            case R.id.select_layout:

                intent = new Intent();
                intent.setClass(PublishStoreActivity.this, SelectClassActivity.class);
                startActivityForResult(intent, REQUEST_MODIFY_SELECT);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data != null) {
        }

        switch (requestCode) {

            case REQUEST_MODIFY_PICTURE:
                Uri imageUri = data.getData();
                Log.i("path", "imageUri.toString=" + imageUri.toString() +
                        ",imageUri.getPath=" + imageUri.getPath() +
                        ",imageUri.getEncodedPath()=" + imageUri.getEncodedPath());
//                    imageUri.getPath(); iv_avatar.setImageURI(imageUri);
                Glide.with(this).load(imageUri).dontTransform().error(R.drawable.avatar_default).into(activity_pic);
                path = FileUtilsOfPaul.getPath(PublishStoreActivity.this, imageUri);
                break;

            case REQUEST_MODIFY_SELECT:

                name = data.getStringExtra("name");
                showToast(name, 1000);
                break;
            default:
                break;
        }
    }

    public void pullPic() {

        File file = new File(path);
        if (!file.exists()) {
            showToast("文件不存在", 1000);
            return;
        }

        startProgressBar("发布中...", new Thread(), false);

        Luban.get(PublishStoreActivity.this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {

                        Map<String, String> params = new HashMap<>();
                        params.put("infoTitle", title_ed.getText().toString());
                        params.put("infoOwner", activity_store_name.getText().toString());
                        params.put("infoPeriod", activity_time.getText().toString());
                        params.put("activeContent", activity_info.getText().toString());
                        params.put("throughUrl", activity_link.getText().toString());
                        params.put("uType", 2 + "");
                        params.put("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""));
                        params.put("localtion", store_address.getText().toString());
                        params.put("category", tag_name);
                        params.put("label", name);

                        OkHttpUtils
                                .post()
                                .url(DailyBuyApplication.IP_URL + "businessPublish.jspa")
                                .addFile("file", "businessPublish.jpg", file)
                                .params(params)
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
                                                showToast(jsonObject.getString("info"), 1000);
                                                finish();
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                }).launch();
    }
}
