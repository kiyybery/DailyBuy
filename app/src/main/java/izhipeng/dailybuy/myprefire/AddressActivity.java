package izhipeng.dailybuy.myprefire;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.adapter.AddressAdapter;
import izhipeng.dailybuy.bean.AddressBean;
import izhipeng.dailybuy.publish.PublishAddressActivity;
import izhipeng.dailybuy.widget.ListViewForScrollView;

/**
 * Created by Administrator on 2016/10/26 0026.
 */
public class AddressActivity extends BaseActivity implements View.OnClickListener {

    ListViewForScrollView listViewForScrollView;
    private AddressBean bean;
    private List<AddressBean> mList;
    private AddressAdapter myAdapter;
    private TextView add_address_tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_activity);

        mList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            bean = new AddressBean();
            bean.setId(1);
            bean.setAddress("北京市朝阳区东大桥路8号尚都国际中心A座2308号");
            bean.setName("孙凯辰");
            bean.setpNum("18965473659");
            mList.add(bean);
        }


        myAdapter = new AddressAdapter(mList, this);
        listViewForScrollView = (ListViewForScrollView) findViewById(R.id.address_listview);
        listViewForScrollView.setAdapter(myAdapter);

        add_address_tv = (TextView) findViewById(R.id.add_address_tv);
        add_address_tv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.add_address_tv:

                Intent intent = new Intent();
                intent.setClass(AddressActivity.this, PublishAddressActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
