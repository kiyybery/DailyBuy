package izhipeng.dailybuy.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class MessageParentFrag extends Fragment {

    public static MessageParentFrag newInstance() {

        MessageParentFrag f = new MessageParentFrag();
        Bundle data = new Bundle();
        data.putString("title", "首页");
        f.setArguments(data);
        return f;
    }
}
