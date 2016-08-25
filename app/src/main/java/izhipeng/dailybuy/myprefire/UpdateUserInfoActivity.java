package izhipeng.dailybuy.myprefire;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class UpdateUserInfoActivity extends AppCompatActivity {

    FragmentManager fm = getSupportFragmentManager();
    FragmentTransaction ft;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {

            ft = fm.beginTransaction();
            UpdateUserInfo productionResultFrg = UpdateUserInfo.newInstance();
            ft.add(android.R.id.content, productionResultFrg);
            ft.commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
