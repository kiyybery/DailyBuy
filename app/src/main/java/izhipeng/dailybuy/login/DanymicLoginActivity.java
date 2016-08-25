package izhipeng.dailybuy.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import net.tsz.afinal.FinalActivity;


import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class DanymicLoginActivity extends FinalActivity {

    private LinearLayout mAccount_layout;
    private int userType;
    private EditText login_username_et, login_pw_et;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danymic_login);
        mAccount_layout = (LinearLayout) findViewById(R.id.account_login_layout);

        userType = getIntent().getIntExtra("userType", 0);

        if (userType == 1) {

            mAccount_layout.setVisibility(View.GONE);
        }

        login_username_et = (EditText) findViewById(R.id.login_username_et);
        login_username_et.setText(PreferencesUtil.get(DailyBuyApplication.KEY_AUTH, ""));
        login_pw_et = (EditText) findViewById(R.id.login_pw_et);
        login_pw_et.setText(PreferencesUtil.get(DailyBuyApplication.KEY_PASSWORD, ""));
    }

}
