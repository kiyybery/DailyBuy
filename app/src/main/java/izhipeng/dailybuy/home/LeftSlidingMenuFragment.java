package izhipeng.dailybuy.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import izhipeng.dailybuy.BaseFragment;
import izhipeng.dailybuy.CourseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.adapter.MyPrefireAdapter;
import izhipeng.dailybuy.bean.MyPrefire;
import izhipeng.dailybuy.bean.UserInform;
import izhipeng.dailybuy.discover.DiscoverNew;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.login.LoginMainActivity;
import izhipeng.dailybuy.myprefire.MyPreFireFragment;
import izhipeng.dailybuy.myprefire.SettingFragment;
import izhipeng.dailybuy.myprefire.UpdateUserInfoActivity;
import izhipeng.dailybuy.share.SocialShareFragment;
import izhipeng.dailybuy.widget.CircleImageView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/10/18 0018.
 */
public class LeftSlidingMenuFragment extends BaseFragment implements View.OnClickListener {

    private UserInform userInform;
    View view;
    private CircleImageView iv_main_left_head;
    private TextView no_login_tv;
    private final static int LOGINTYPE_RESULT = 10;

    private int[] img = new int[]{
            R.drawable.my_coin_icon, R.drawable.my_publish_icon,
            R.drawable.my_setting_icon,
            R.drawable.my_share_icon
    };

    private int[] imgs = new int[]{

            R.mipmap.slidingmenu_icon_home, R.mipmap.slidingmenu_icon_gift,
            R.mipmap.slidingmenu_icon_setting, R.mipmap.slidingmenu_icon_welcome
    };

    private String[] texts = new String[]{
            "首页", "我的奖品",
            "设置", "邀请好友"
    };

    private List<MyPrefire> mList = new ArrayList<>();
    private ListView mListView;
    private MyPrefireAdapter mAdapter;
    private LinearLayout login_layout;

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.main_left_fragment, container,
                false);
        iv_main_left_head = (CircleImageView) view.findViewById(R.id.iv_main_left_head);
        no_login_tv = (TextView) view.findViewById(R.id.no_login_tv);
        if (!TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_URL, ""))) {

            Glide.with(getActivity())
                    .load(PreferencesUtil.get(DailyBuyApplication.KEY_URL, ""))
                    .error(R.drawable.kuangge)
                    .placeholder(R.drawable.kuangge)
                    .dontAnimate()
                    .into(iv_main_left_head);

            no_login_tv.setText(PreferencesUtil.get(DailyBuyApplication.KEY_NAME, ""));
        }
        login_layout = (LinearLayout) view.findViewById(R.id.login_layout);
        login_layout.setOnClickListener(this);
        mListView = (ListView) view.findViewById(R.id.mListView);
        mAdapter = new MyPrefireAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Fragment newContent = null;
                if (i == 0) {

                    /*if (!checkNeedLogin()) {

                        return;
                    } else {
                        newContent = new MyPreFireFragment();
                    }*/
                    newContent = new HomeMain();
                } else if (i == 1) {

                    newContent = new MyGiftFragment();
                } else if (i == 2) {

                    newContent = new SettingFragment();
                } else if (i == 3) {

                    showSpinner();
                } else if (i == 4) {

                    newContent = new SocialShareFragment();
                    SocialShareFragment socialShareFragment = new SocialShareFragment();

                }

                if (newContent != null) {

                    switchFragment(newContent);
                }

                mAdapter.setSelectedPosition(i);
                mAdapter.notifyDataSetInvalidated();
            }
        });
        return view;
    }

    private void switchFragment(Fragment fragment) {
        if (getActivity() == null)
            return;
        CourseActivity ra = (CourseActivity) getActivity();
        ra.switchContent(fragment);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.login_layout:
                Intent intent;

                if (TextUtils.isEmpty(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))) {

                    intent = new Intent();
                    intent.setClass(getActivity(), LoginMainActivity.class);
                    startActivityForResult(intent, LOGINTYPE_RESULT);

                } else {

                    intent = new Intent();
                    intent.setClass(getActivity(), UpdateUserInfoActivity.class);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }

    }

    private void showSpinner() {

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.social_share_fragment, null);
        //RelayoutViewTool.relayoutViewWithScale(v, YanHao.screenWidthScale);
        final PopupWindow pw = new PopupWindow(v, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        pw.setContentView(v);
        pw.setOutsideTouchable(false);
        pw.setFocusable(true);
        pw.setBackgroundDrawable(new BitmapDrawable());
//        pw.showAsDropDown(v);
        pw.showAtLocation(view, Gravity.BOTTOM, 0, -100);

        Button btn_cancel_social_share = (Button) v.findViewById(R.id.btn_cancel_social_share);
        btn_cancel_social_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
                //switchFragment(new HomeMain());
            }
        });

        Button share_to_weixin = (Button) v.findViewById(R.id.share_to_weixin);
        share_to_weixin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareAction shareAction = new ShareAction(getActivity());
                shareAction.withText("摇摇乐");
                shareAction.withMedia(new UMImage(getActivity(), ""));
                shareAction.withTitle("yaoyaole");
                shareAction.withTargetUrl("");
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN)
                        .setCallback(umShareListener).share();
            }
        });

        Button share_to_wxcircle = (Button) v.findViewById(R.id.share_to_wxcircle);
        share_to_wxcircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareAction shareAction = new ShareAction(getActivity());
                shareAction.withText("");
                shareAction.withMedia(new UMImage(getActivity(), ""));
                shareAction.withTitle("");
                shareAction.withTargetUrl("");
                shareAction.setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setCallback(umShareListener).share();
            }
        });

        Button share_to_qq = (Button) v.findViewById(R.id.share_to_qq);
        share_to_qq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareAction shareAction = new ShareAction(getActivity());
                shareAction.withText("");
                shareAction.withMedia(new UMImage(getActivity(), ""));
                shareAction.withTitle("");
                shareAction.withTargetUrl("");
                shareAction.setPlatform(SHARE_MEDIA.QQ)
                        .setCallback(umShareListener).share();
            }
        });
    }

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            com.umeng.socialize.utils.Log.d("plat", "platform" + platform);

            OkHttpUtils
                    .post()
                    .url(DailyBuyApplication.IP_URL + "updateFavorableShareAndLike.jspa")
                    .addParams("userId", PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""))
                    .addParams("infoId", 1 + "")
                    .addParams("iType", 1 + "")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int i) {

                        }

                        @Override
                        public void onResponse(String s, int i) {

                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                if (jsonObject.getInt("ret") == 1) {

                                    Toast.makeText(getActivity(), jsonObject.getString("info"), Toast.LENGTH_LONG)
                                            .show();
                                } else {

                                    Toast.makeText(getActivity(), jsonObject.getString("info"), Toast.LENGTH_LONG)
                                            .show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(getActivity(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            Log.e("error", t + "");

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(getActivity(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (data != null) {
        }
        switch (requestCode) {

            case LOGINTYPE_RESULT:

                Glide.with(getActivity())
                        .load(PreferencesUtil.get(DailyBuyApplication.KEY_URL, ""))
                        .error(R.drawable.kuangge)
                        .placeholder(R.drawable.kuangge)
                        .dontAnimate()
                        .into(iv_main_left_head);

                no_login_tv.setText(PreferencesUtil.get(DailyBuyApplication.KEY_NAME, ""));

                break;
            default:
                break;
        }
    }
}
