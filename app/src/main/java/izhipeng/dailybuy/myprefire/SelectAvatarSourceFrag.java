package izhipeng.dailybuy.myprefire;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import izhipeng.dailybuy.R;

/**
 * Created by Administrator on 2015/11/12 0012.
 */
public class SelectAvatarSourceFrag extends DialogFragment implements View.OnClickListener {

    private static final String TAG = SelectAvatarSourceFrag.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private String mCurrentPhotoPath;

    public static SelectAvatarSourceFrag newInstance() {
        SelectAvatarSourceFrag fragment = new SelectAvatarSourceFrag();
        return fragment;
    }

    public SelectAvatarSourceFrag() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int style = DialogFragment.STYLE_NO_FRAME, theme = 0;//神奇
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().setCanceledOnTouchOutside(true);

        View rooView = inflater.inflate(R.layout.dialog_select_avater_source, container, false);
        Button btnCamera = (Button) rooView.findViewById(R.id.btn_from_camera);
        Button btnSDcard = (Button) rooView.findViewById(R.id.btn_from_sd);
        Button btnCancel = (Button) rooView.findViewById(R.id.btn_cancel_change_avatar);
        btnCamera.setOnClickListener(this);
        btnSDcard.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        return rooView;

    }

    @Override
    public void onStart() {
        super.onStart();
        // safety check
//        if (getDialog() == null) {
//            return;
//        }


        Dialog d = getDialog();
        d.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        d.getWindow().setGravity(Gravity.CENTER_HORIZONTAL);
    }

    ArrayAdapter<String> projectAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
//      todo
        Log.i(TAG, "onAttach");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean hasCameraPemission() {

        String permission = "android.permission.CAMERA";
        int res = getActivity().checkCallingOrSelfPermission(permission);
        return (res == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_from_camera:
                if (!hasCameraPemission()) {
                    Toast.makeText(getActivity(), "没有权限，请先在系统设置中开启相机权限", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getActivity().getPackageManager()) == null) {
                    // Create the File where the photo sho
                    // uld go
                    Log.e(TAG, "该手机中没有对应的相机权限");
                    return;
                }
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    // Error occurred while creating the File
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    startActivityForResult(intent, UpdateUserInfo.REQUEST_MODIFY_AVATAR_CAMERA);
                }
                break;
            case R.id.btn_from_sd:
                intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, UpdateUserInfo.REQUEST_MODIFY_AVATAR_SD);
                break;
            case R.id.btn_cancel_change_avatar:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) {
                data = new Intent();
            }
            Log.i(TAG, "data=" + data.toString());
            if (requestCode == UpdateUserInfo.REQUEST_MODIFY_AVATAR_CAMERA) {
                data.putExtra("avatar", 1);
                data.putExtra("avatarPath", mCurrentPhotoPath);
            } else {
                data.putExtra("avatar", 2);
            }
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
        }
        dismiss();
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
