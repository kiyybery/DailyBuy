package izhipeng.dailybuy.myprefire;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import izhipeng.dailybuy.BaseActivity;
import izhipeng.dailybuy.DailyBuyApplication;
import izhipeng.dailybuy.R;
import izhipeng.dailybuy.library.PreferencesUtil;

/**
 * Created by Administrator on 2016/11/14 0014.
 */
public class UpdateNameActivity extends Activity {

    private ImageView finish_icon;
    private EditText serach_et;
    private Button rename_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rename);

        finish_icon = (ImageView) findViewById(R.id.finish_icon);
        finish_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        serach_et = (EditText) findViewById(R.id.serach_et);
        rename_btn = (Button) findViewById(R.id.rename_btn);
        rename_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferencesUtil.put(DailyBuyApplication.KEY_NAME, serach_et.getText().toString());
                Intent intent = new Intent();
                intent.putExtra("username", serach_et.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}
