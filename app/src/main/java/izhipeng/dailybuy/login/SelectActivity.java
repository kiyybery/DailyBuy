package izhipeng.dailybuy.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.List;

import izhipeng.dailybuy.R;
import izhipeng.dailybuy.adapter.SelectActivityAdapter;
import izhipeng.dailybuy.bean.SelectLoginBean;

/**
 * Created by Administrator on 2016/7/26 0026.
 */
public class SelectActivity extends AppCompatActivity {

    private GridView mGridView;
    private String[] types = new String[]{"我是用户", "我是商家"};
    private List<SelectLoginBean> mList = new ArrayList<SelectLoginBean>();
    private SelectActivityAdapter mAdapter;
    private int consultType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_login);
        mGridView = (GridView) findViewById(R.id.select_grid);

        for (int i = 0; i < types.length; i++) {

            SelectLoginBean bean = new SelectLoginBean();
            bean.setText(types[i]);
            mList.add(bean);
        }

        mAdapter = new SelectActivityAdapter(SelectActivity.this, mList);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 1) {

                    consultType = i;
                    Intent intent = new Intent();
                    intent.putExtra("userType", i);
                    intent.setClass(SelectActivity.this, LoginMainActivity.class);
                    startActivity(intent);
                } else {

                    consultType = i;
                    Intent intent = new Intent();
                    intent.putExtra("userType", i);
                    intent.setClass(SelectActivity.this, LoginMainActivity.class);
                    startActivity(intent);
                }

                mAdapter.setSelectedPosition(i);
                mAdapter.notifyDataSetInvalidated();
            }
        });
    }
}
