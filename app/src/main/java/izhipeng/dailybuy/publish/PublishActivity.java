package izhipeng.dailybuy.publish;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
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
 * Created by Administrator on 2016/8/18 0018.
 */
public class PublishActivity extends BaseActivity {

    private LinearLayout ll_section_title_back, ll_section_title_right, tag_layout, select_layout;
    private TextView tv_section_title_title, tv_section_title_right;
    private ImageView activity_pic;
    private EditText title_ed, activity_info, activity_store_name,
            activity_time, activity_link;
    private String path;

    public static final int REQUEST_MODIFY_PICTURE = 200;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_publish);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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
        activity_info.setSingleLine(false);
        activity_info.setHorizontallyScrolling(false);
        activity_store_name = (EditText) findViewById(R.id.activity_store_name);
        activity_time = (EditText) findViewById(R.id.activity_time);
        activity_link = (EditText) findViewById(R.id.activity_link);
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
                path = FileUtilsOfPaul.getPath(PublishActivity.this, imageUri);
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

        Luban.get(PublishActivity.this)
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
                        params.put("uType", 1 + "");
                        params.put("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""));

                        OkHttpUtils
                                .post()
                                .url(DailyBuyApplication.IP_URL + "userPublish.jspa")
                                .addFile("file", "publish.jpg", file)
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

                        Log.e("pic_publish", "error");
                    }
                }).launch();
    }
}
