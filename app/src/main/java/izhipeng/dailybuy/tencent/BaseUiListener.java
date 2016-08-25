package izhipeng.dailybuy.tencent;

import org.json.JSONObject;

import android.widget.Toast;


import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;

import izhipeng.dailybuy.BaseActivity;

public class BaseUiListener implements IUiListener {

	private BaseActivity context;
	
	public BaseUiListener(BaseActivity context) {
		this.context = context;
	}
	@Override
	public void onCancel() {
		context.showToast("取消操作", Toast.LENGTH_SHORT);
		
	}
	@Override
	public void onComplete(Object response) {
		doComplete((JSONObject)response);
	}
	
	protected void doComplete(JSONObject values) {
		
	}

	@Override
	public void onError(UiError e) {
		context.showToast("错误："+"code:" + e.errorCode + ", msg:"
				+ e.errorMessage + ", detail:" + e.errorDetail, Toast.LENGTH_SHORT);
	}
}
