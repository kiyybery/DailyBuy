package izhipeng.dailybuy.widget;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import izhipeng.dailybuy.R;


public class AlertDialogFrag extends DialogFragment {
    @NonNull
    public static AlertDialogFrag newInstance(Bundle args) {
        AlertDialogFrag f = new AlertDialogFrag();
        f.setArguments(args);
        return f;
    }

    /**
     * This theme is applied on top of the current theme in
     * <var>context</var>.  If 0, the default dialog theme will be used.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
//        int style = getArguments().getInt("style");
//        int theme = 0;//神奇

        int style = DialogFragment.STYLE_NO_FRAME, theme = android.R.style.Theme_Translucent;//神奇
        setStyle(style, theme);
        super.onCreate(savedInstanceState);
    }

    Button btnOk, btnCancel;
    TextView titleView, AlertMsg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.frag_alertdialog, container, false);
//        todo  init btnOk btnCancel titleView, AlertMsg
        titleView = (TextView) root.findViewById(R.id.alertTitle);
        AlertMsg = (TextView) root.findViewById(R.id.aletMsg);
        btnOk = (Button) root.findViewById(R.id.alertConfirm);
        btnCancel = (Button) root.findViewById(R.id.alertCanCel);

        final Intent data = new Intent();
        Bundle args = getArguments();
        String title = args.getString("title", "");
        String message = args.getString("msg", "");

        if (!TextUtils.isEmpty(title)) {
            titleView.setVisibility(View.VISIBLE);
            titleView.setText(title);
        }
        if (!TextUtils.isEmpty(message)) {
            AlertMsg.setText(message);
        }
        data.putExtras(args);

        btnOk.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
                                         dismiss();//退不回去就用onBackPressed
                                     }
                                 }
        );
        btnCancel.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_CANCELED, data);
                                             dismiss();//退不回去就用onBackPressed
                                         }
                                     }
        );


        return root;
    }


    public interface ConfirmListener {
        String getPositiveHint();

        void onPositive(DialogInterface dialog);

        String getNegativeHint();

        void onNegative(DialogInterface dialog);
    }


}