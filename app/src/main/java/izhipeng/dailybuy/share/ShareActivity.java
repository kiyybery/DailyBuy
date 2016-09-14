package izhipeng.dailybuy.share;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.sso.UMSsoHandler;

import izhipeng.dailybuy.R;

/**
 * Created by Administrator on 2015/12/11 0011.
 */
public class ShareActivity extends AppCompatActivity {

    String shareTitle, shareDesp, shareImageUrl, shareWebUrl, infoId;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        shareTitle = getIntent().getStringExtra("shareTitle");
        shareDesp = getIntent().getStringExtra("shareDesp");
        shareImageUrl = getIntent().getStringExtra("shareImageUrl");
        shareWebUrl = getIntent().getStringExtra("shareWebUrl");
        infoId = getIntent().getStringExtra("infoId");


        Bundle shareData = new Bundle();
        shareData.putString("shareTitle", shareTitle);
        shareData.putString("shareDesp", shareDesp);
        shareData.putString("shareImageUrl", shareImageUrl);
        shareData.putString("shareWebUrl", shareWebUrl);
        shareData.putString("infoId", infoId);


        setContentView(R.layout.activity_friend_share);

        SocialShareFragment f = SocialShareFragment.getInstance();
        f.setArguments(shareData);
        getSupportFragmentManager().beginTransaction().replace(R.id.container_share, f, null).commit();
    }

    final UMSocialService mController = UMServiceFactory
            .getUMSocialService("com.umeng.share");

    public UMSocialService getUMSocialService() {
        return mController;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /** 使用SSO授权必须添加如下代码 */

        UMSocialService mController = getUMSocialService();
        UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
                requestCode);
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
