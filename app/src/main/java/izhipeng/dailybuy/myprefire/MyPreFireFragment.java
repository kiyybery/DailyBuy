package izhipeng.dailybuy.myprefire;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.channels.NonReadableChannelException;
import java.util.ArrayList;
import java.util.List;

import izhipeng.dailybuy.BaseFragment;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.adapter.MyPrefireAdapter;
import izhipeng.dailybuy.bean.MyPrefire;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.login.LoginMainActivity;
import izhipeng.dailybuy.share.ShareActivity;
import izhipeng.dailybuy.widget.ListViewForScrollView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/3 0003.
 */
public class MyPreFireFragment extends BaseFragment implements View.OnClickListener {

    private int[] imgs = new int[]{
            R.drawable.my_coin_icon, R.drawable.my_publish_icon,
            R.drawable.my_collection_icon, R.drawable.my_setting_icon,
            R.drawable.my_share_icon
    };

    private String[] texts = new String[]{
            "积分商城", "我的发布", "我的收藏",
            "设置", "应用推荐给好友"
    };
    public static final int REQUEST_MODIFY_LOGOUT = 100;
    public static final int REQUEST_MODIFY_UPDATE = 101;
    public static final int REQUEST_MODIFY_LOGIN = 102;
    private List<MyPrefire> mList = new ArrayList<>();
    private ListViewForScrollView mListView;

    private MyPrefireAdapter mAdapter;
    private ImageView my_img_pro;
    private TextView name_tv, credits_tv;
    private LinearLayout coin_layout;
    private String credits, dayCredits;

    public static MyPreFireFragment newInstance() {

        MyPreFireFragment f = new MyPreFireFragment();
        Bundle data = new Bundle();
        data.putString("title", "首页");
        f.setArguments(data);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        for (int i = 0; i < texts.length; i++) {
            MyPrefire my = new MyPrefire();
            my.setImg(imgs[i]);
            my.setText(texts[i]);
            mList.add(my);
        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_my, container, false);
        //View headView = getActivity().getLayoutInflater().inflate(R.layout.my_head_view, null);
        my_img_pro = (ImageView) view.findViewById(R.id.my_img_pro);
        my_img_pro.setOnClickListener(this);
        if (!TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {

            getUserInfo();
        }
        Glide.with(getActivity())
                .load(PreferencesUtil.get(DailyBuyApplication.KEY_URL, ""))
                .placeholder(R.drawable.avatar_default)
                .error(R.drawable.avatar_default)
                .dontTransform()
                .dontAnimate()
                .into(my_img_pro);
        name_tv = (TextView) view.findViewById(R.id.my_text_name);
        name_tv.setText(PreferencesUtil.get(DailyBuyApplication.KEY_NAME, ""));
        coin_layout = (LinearLayout) view.findViewById(R.id.coin_layout);
        coin_layout.setOnClickListener(this);
        credits_tv = (TextView) view.findViewById(R.id.credits_tv);
        credits_tv.setText(credits);
        mListView = (ListViewForScrollView) view.findViewById(R.id.mListView);
        //mListView.addHeaderView(headView);
        mAdapter = new MyPrefireAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Log.i("posion", i + "");
                Intent intent;
                if (i == 0) {

                    showToast("积分商城", 1000);
                    if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {

                        intent = new Intent();
                        intent.setClass(getActivity(), LoginMainActivity.class);
                        startActivityForResult(intent, REQUEST_MODIFY_LOGIN);
                    } else {
                        intent = new Intent();
                        intent.putExtra("webUrl", DailyBuyApplication.IP_URL + "getOnlineGiftList.jspa?pageId=1&userId="
                                + PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""));
                        intent.setClass(getActivity(), OnLineGiftWebView.class);
                        startActivity(intent);
                    }
                } else if (i == 1) {

                    showToast("我的发布", 1000);

                    if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {

                        intent = new Intent();
                        intent.setClass(getActivity(), LoginMainActivity.class);
                        startActivityForResult(intent, REQUEST_MODIFY_LOGIN);

                    } else {
                        intent = new Intent();
                        intent.setClass(getActivity(), MyPublishActivity.class);
                        startActivity(intent);
                    }
                } else if (i == 2) {

                    showToast("我的收藏", 1000);
                    if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {

                        intent = new Intent();
                        intent.setClass(getActivity(), LoginMainActivity.class);
                        startActivityForResult(intent, REQUEST_MODIFY_LOGIN);
                    } else {
                        intent = new Intent();
                        intent.setClass(getActivity(), MyCollectionActivity.class);
                        startActivity(intent);
                    }
                } else if (i == 3) {

                    showToast("设置", 1000);
                    intent = new Intent();
                    intent.setClass(getActivity(), SettingActivity.class);
                    startActivityForResult(intent, REQUEST_MODIFY_LOGOUT);

                } else if (i == 4) {

                    showToast("推荐", 1000);
                    intent = new Intent();
                    intent.putExtra("shareTitle", "APP DOWNLOAD");
                    intent.putExtra("shareDesp", "APP DOWNLOAD");
                    intent.putExtra("shareImageUrl", "APP DOWNLOAD");
                    intent.putExtra("shareWebUrl", "APP DOWNLOAD");
                    intent.setClass(getActivity(), ShareActivity.class);
                    startActivity(intent);
                }
            }
        });
        return view;
    }

    private void getUserInfo() {
        Log.e("MyPreFireFragment", "this getUserInfo");
        Log.e("MyPreFireFragment", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, "") + "is null ");

        if (PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, "").equals("")) {
            new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    String s = PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, "");
                    Log.e("MyPreFireFragment", s + " two is null");
                    initNetwork();
                }
            }.sendEmptyMessageDelayed(0, 1000);
        } else {
            initNetwork();
        }

//        initNetwork();
    }

    private void initNetwork() {
        OkHttpUtils
                .post()
                .url(DailyBuyApplication.IP_URL + "getUserInfo.jspa")
                        //.addParams("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))
                .addParams("userId", "OUjd5UidGVE=")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int i) {
                        showToast(e + "", 1000);
                        Log.e("MyPreFireFragment", "this getUserInfo onError");
                    }

                    @Override
                    public void onResponse(String s, int i) {
                        Log.e("MyPreFireFragment", "this getUserInfo onResponse");
                        Log.e("MyPreFireFragment", s);
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            if (jsonObject.getInt("ret") == 2) {

                                showToast(jsonObject.getString("info") + "", 1000);
                            } else {

                                Glide.with(getActivity())
                                        .load(jsonObject.getString("portraitUrl"))
                                        .dontTransform().dontAnimate()
                                        .placeholder(R.drawable.avatar_default)
                                        .error(R.drawable.avatar_default).into(my_img_pro);
                                name_tv.setText(jsonObject.getString("nickName"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_AUTH, jsonObject.getString("userId"));
                                PreferencesUtil.put(DailyBuyApplication.KEY_TYPE, jsonObject.getInt("userType"));
                                credits = jsonObject.getString("credits");
                                credits_tv.setText(credits);
                                dayCredits = jsonObject.getString("dayCredits");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.my_img_pro:
                if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginMainActivity.class);
                    startActivityForResult(intent, REQUEST_MODIFY_LOGIN);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(), UpdateUserInfoActivity.class);
                    startActivityForResult(intent, REQUEST_MODIFY_UPDATE);
                }
                break;

            case R.id.coin_layout:

                if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {

                    Intent intent = new Intent();
                    intent.setClass(getActivity(), LoginMainActivity.class);
                    startActivityForResult(intent, REQUEST_MODIFY_LOGIN);
                } else {
                    Intent intent = new Intent();
                    intent.putExtra("credits", credits);
                    intent.putExtra("dayCredits", dayCredits);
                    intent.setClass(getActivity(), MyCoinActivity.class);
                    startActivity(intent);
                }
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

            case REQUEST_MODIFY_LOGOUT:
                PreferencesUtil.put(DailyBuyApplication.KEY_AUTH, "");
                Glide.with(getActivity()).load(R.drawable.avatar_default)
                        .dontAnimate().dontTransform()
                        .into(my_img_pro);
                name_tv.setText("");
                break;

            case REQUEST_MODIFY_UPDATE:
                getUserInfo();
                break;

            case REQUEST_MODIFY_LOGIN:
                getUserInfo();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("MyPreFireFragment", "onResume");

        getUserInfo();
        /*name_tv.setText(PreferencesUtil.get(DailyBuyApplication.KEY_NAME, ""));
        Glide.with(getActivity())
                .load(PreferencesUtil.get(DailyBuyApplication.KEY_URL, ""))
                .placeholder(R.drawable.avatar_default)
                .error(R.drawable.avatar_default)
                .dontTransform()
                .dontAnimate()
                .into(my_img_pro);*/
    }

    @Override
    public void onStart() {
        super.onStart();

//        getUserInfo();
    }
}
