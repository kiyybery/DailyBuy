package izhipeng.dailybuy.myprefire;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import izhipeng.dailybuy.BaseFragment;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;
import izhipeng.dailybuy.library.UIHelper;

/**
 * Created by Administrator on 2016/10/19 0019.
 */
public class SettingFragment extends BaseFragment implements View.OnClickListener {

    private LinearLayout mBack;
    private TextView mTitle;
    private RelativeLayout mNotification_layout, mSafe_layout, mClear_layout,
            mContact_layout, mAbout_layout, mLogout_layout;
    private Dialog dialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_setting, container, false);

        mNotification_layout = (RelativeLayout) view.findViewById(R.id.message_layout);
        mNotification_layout.setOnClickListener(this);
        mSafe_layout = (RelativeLayout) view.findViewById(R.id.safe_layout);
        mSafe_layout.setOnClickListener(this);
        mClear_layout = (RelativeLayout) view.findViewById(R.id.clear_layout);
        mClear_layout.setOnClickListener(this);
        mContact_layout = (RelativeLayout) view.findViewById(R.id.contact_layout);
        mContact_layout.setOnClickListener(this);
        mAbout_layout = (RelativeLayout) view.findViewById(R.id.about_layout);
        mAbout_layout.setOnClickListener(this);
        mLogout_layout = (RelativeLayout) view.findViewById(R.id.logout_layout);
        mLogout_layout.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.message_layout:
                Intent intent = new Intent();
                intent.setClass(getActivity(), NotifyCtrlActivity.class);
                startActivity(intent);
                break;
            case R.id.safe_layout:

                intent = new Intent();
                intent.setClass(getActivity(), PrivacyActivity.class);
                startActivity(intent);
                break;
            case R.id.contact_layout:

                dialog = UIHelper.buildConfirm(getActivity(), "确认联系客服？",
                        "确认", "取消", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();

                                getActivity().finish();
                            }
                        }, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                dialog.dismiss();
                            }
                        });
                break;
            case R.id.about_layout:

                intent = new Intent();
                intent.setClass(getActivity(), AboutActivity.class);
                startActivity(intent);
                break;

            case R.id.logout_layout:

                PreferencesUtil.put(DailyBuyApplication.KEY_AUTH, "");
                //PreferencesUtil.put(DailyBuyApplication.KEY_NAME, "");
                //PreferencesUtil.put(DailyBuyApplication.KEY_SEX, 0);
                //PreferencesUtil.put(DailyBuyApplication.KEY_CITY, "");
                //PreferencesUtil.put(DailyBuyApplication.KEY_PATH, "");
                intent = new Intent();
                getActivity().setResult(getActivity().RESULT_OK, intent);
                getActivity().finish();
                break;
            default:
                break;
        }
    }
}
