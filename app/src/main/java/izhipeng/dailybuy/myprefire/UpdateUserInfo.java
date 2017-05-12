package izhipeng.dailybuy.myprefire;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import izhipeng.dailybuy.BaseFragment;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.home.HomeDetialActivity;
import izhipeng.dailybuy.library.FileUtilsOfPaul;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.widget.CircleImageView;
import okhttp3.Call;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class UpdateUserInfo extends BaseFragment implements View.OnClickListener {

    private LinearLayout mBack;
    private TextView mTitle, edit_name, edit_sex, edit_city;
    private RelativeLayout avatar_layout, name_layout, sex_layout, address_layout;
    private static final String TAG = UpdateUserInfo.class.getSimpleName();
    private static final int REQUEST_MODIFY_AVATAR = 500;
    public static final int REQUEST_MODIFY_AVATAR_CAMERA = 501;
    public static final int REQUEST_MODIFY_AVATAR_SD = 502;
    public static final int REQUEST_MODIFY_UNAME = 503;
    public static final int REAQEST_MODIFY_SEX = 504;
    public static final int REQUEST_MODIFY_CITY = 505;
    private CircleImageView mCircleImageView;
    private String path;

    public static UpdateUserInfo newInstance() {

        UpdateUserInfo fragment = new UpdateUserInfo();
        Bundle data = new Bundle();
        data.putString("title", " ");
        fragment.setArguments(data);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_update, container, false);
        mBack = (LinearLayout) view.findViewById(R.id.ll_section_title_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUserInfo();
                Intent intent = new Intent();
                finish(Activity.RESULT_OK, intent);
            }
        });
        mTitle = (TextView) view.findViewById(R.id.tv_section_title_title);
        mTitle.setText("编辑资料");
        mCircleImageView = (CircleImageView) view.findViewById(R.id.iv_uc_avatar_edit);
        avatar_layout = (RelativeLayout) view.findViewById(R.id.avatar_layout);
        name_layout = (RelativeLayout) view.findViewById(R.id.name_layout);
        sex_layout = (RelativeLayout) view.findViewById(R.id.sex_layout);
        address_layout = (RelativeLayout) view.findViewById(R.id.address_layout);
        Glide.with(this)
                .load(PreferencesUtil.get(DailyBuyApplication.KEY_URL, ""))
                .dontAnimate()
                .dontTransform()
                .error(R.drawable.avatar_default)
                .placeholder(R.drawable.avatar_default)
                .into(mCircleImageView);
        edit_name = (TextView) view.findViewById(R.id.edit_name);
        edit_name.setText(PreferencesUtil.get(DailyBuyApplication.KEY_NAME, ""));
        edit_sex = (TextView) view.findViewById(R.id.edit_sex);
        if (PreferencesUtil.get(DailyBuyApplication.KEY_SEX, 0) == 1) {

            edit_sex.setText("女");
        } else if (PreferencesUtil.get(DailyBuyApplication.KEY_SEX, 0) == 2) {

            edit_sex.setText("男");
        }
        edit_city = (TextView) view.findViewById(R.id.edit_address);
        edit_city.setText(PreferencesUtil.get(DailyBuyApplication.KEY_CITY, ""));
        avatar_layout.setOnClickListener(this);
        name_layout.setOnClickListener(this);
        sex_layout.setOnClickListener(this);
        address_layout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.avatar_layout:
                FragmentManager fm = getFragmentManager();
                SelectAvatarSourceFrag f = SelectAvatarSourceFrag.newInstance();
                f.setTargetFragment(UpdateUserInfo.this, REQUEST_MODIFY_AVATAR);
                f.show(fm, "avatar");
                break;
            case R.id.name_layout:

                Intent intent = new Intent();
                intent.setClass(getActivity(), UpdateNameActivity.class);
                startActivityForResult(intent, REQUEST_MODIFY_UNAME);
                break;
            case R.id.sex_layout:

                /*intent = new Intent();
                intent.setClass(getActivity(), UpdateSex.class);
                startActivityForResult(intent, REAQEST_MODIFY_SEX);*/
                intent = new Intent();
                intent.setClass(getActivity(), HomeDetialActivity.class);
                intent.putExtra("webUrl", DailyBuyApplication.IP_URL + "getReceiverAddrListForIos.jspa?userId=e2os3NIaWaA=");
                startActivity(intent);
                break;

            case R.id.address_layout:

                intent = new Intent();
                intent.setClass(getActivity(), CityListActivity.class);
                startActivityForResult(intent, REQUEST_MODIFY_CITY);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data != null) {
        }
        switch (requestCode) {

            case REQUEST_MODIFY_AVATAR:
                /**
                 *  拍照后是跳转到裁剪frag,再到预览frag, 然后在预览frag if(确定)，再次返回本界面,更新本地avatar,
                 *   if(onBackPressed 用户确认更新个人资料|点击保存按钮)
                 * 从相册选择后返回，类似于拍照., 暂时先不做裁剪了，有时间再做
                 */
                int type = data.getIntExtra("avatar", 0);

                if (type == 1) {
                    Bundle extras = data.getExtras();
                    path = extras.getString("avatarPath");
                    if (!TextUtils.isEmpty(path)) {
                        Log.i(TAG, "从相机返回的头像地址" + path);
                        Glide.with(this).load(Uri.fromFile(new File(path))).dontTransform().error(R.drawable.avatar_default).into(mCircleImageView);
                    }
                    updatepotrait();
                } else if (type == 2) {
                    Uri imageUri = data.getData();
                    Log.i(TAG, "imageUri.toString=" + imageUri.toString() +
                            ",imageUri.getPath=" + imageUri.getPath() +
                            ",imageUri.getEncodedPath()=" + imageUri.getEncodedPath());
//                    imageUri.getPath(); iv_avatar.setImageURI(imageUri);
                    Glide.with(this).load(imageUri).dontTransform().error(R.drawable.avatar_default).into(mCircleImageView);
                    path = FileUtilsOfPaul.getPath(getActivity(), imageUri);
                    Log.i(TAG, "从本地Uri解析出的头像地址" + path);
                    //uploadImg(imageUri);
                    updatepotrait();
                }

                PreferencesUtil.put(DailyBuyApplication.KEY_PATH, path);
                break;
            case REQUEST_MODIFY_UNAME:

                //edit_name.setText(data.getStringExtra("username"));
                edit_name.setText(PreferencesUtil.get(DailyBuyApplication.KEY_NAME, ""));
                break;

            case REAQEST_MODIFY_SEX:

                if (PreferencesUtil.get(DailyBuyApplication.KEY_SEX, 0) == 1) {

                    edit_sex.setText("女");
                } else {

                    edit_sex.setText("男");
                }
                break;
            case REQUEST_MODIFY_CITY:

                edit_city.setText(PreferencesUtil.get(DailyBuyApplication.KEY_CITY, ""));
                break;

            default:
                break;
        }
    }

    private void updatepotrait() {

        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(getActivity(), "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        startProgressBar("上传头像...", new Thread(), true);

        Luban.get(getActivity())
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        Map<String, String> params = new HashMap<>();
                        params.put("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""));
                        OkHttpUtils
                                .post()
                                .url(DailyBuyApplication.IP_URL + "updateUserPotrait.jspa")
                                .addFile("file", "avatar.png", file)
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
                                            if (jsonObject.getInt("ret") == 1) {
                                                closeProgressBar();
                                                showToast(jsonObject.getString("info"), 1000);
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

                    @Override
                    public void onError(Throwable throwable) {

                    }
                }).launch();
    }

    private void updateUserInfo() {

        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "updateUserInfo.jspa")
                .addParams("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))
                .addParams("nickName", PreferencesUtil.get(DailyBuyApplication.KEY_NAME, ""))
                .addParams("sex", PreferencesUtil.get(DailyBuyApplication.KEY_SEX, 0) + "")
                .addParams("city", PreferencesUtil.get(DailyBuyApplication.KEY_CITY, ""))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        showToast(e + "", 1000);
                    }

                    @Override
                    public void onResponse(String s, int i) {

                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("ret") == 1) {
                                showToast(jsonObject.getString("info") + "", 1000);
                            } else {
                                showToast(jsonObject.getString("info") + "", 1000);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
