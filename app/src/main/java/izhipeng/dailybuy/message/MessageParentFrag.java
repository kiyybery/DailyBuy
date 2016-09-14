package izhipeng.dailybuy.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import izhipeng.dailybuy.BaseFragment;
import izhipeng.dailybuy.R;

/**
 * Created by Administrator on 2016/8/11 0011.
 */
public class MessageParentFrag extends BaseFragment {

    private TextView tv_section_title_title;

    public static MessageParentFrag newInstance() {

        MessageParentFrag f = new MessageParentFrag();
        Bundle data = new Bundle();
        data.putString("title", "首页");
        f.setArguments(data);
        return f;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_message, container, false);
        tv_section_title_title = (TextView) view.findViewById(R.id.tv_section_title_title);
        tv_section_title_title.setText("消息");
        return view;
    }
}
