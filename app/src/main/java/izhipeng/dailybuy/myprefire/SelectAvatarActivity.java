package izhipeng.dailybuy.myprefire;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import izhipeng.dailybuy.BaseActivity;

/**
 * Created by Administrator on 2016/8/15 0015.
 */
public class SelectAvatarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {

            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            SelectAvatarSourceFrag f = SelectAvatarSourceFrag.newInstance();
            ft.add(android.R.id.content, f);
            ft.commit();
        }
    }
}
