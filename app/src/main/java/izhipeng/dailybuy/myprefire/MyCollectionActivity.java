package izhipeng.dailybuy.myprefire;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.bean.MyCollection;
import izhipeng.dailybuy.widget.PushListView;
import izhipeng.dailybuy.widget.SlidingTabLayoutForOrder;
import izhipeng.dailybuy.widget.TabsAdapter;
import izhipeng.dailybuy.widget.XListView;
import okhttp3.Call;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class MyCollectionActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.avtivity_mycollection);

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        MyCollectionFragment orderAllFragment = new MyCollectionFragment();
        ft.add(android.R.id.content, orderAllFragment);
        ft.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
