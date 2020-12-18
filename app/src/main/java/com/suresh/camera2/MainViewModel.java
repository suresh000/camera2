package com.suresh.camera2;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

public class MainViewModel {

    private static final int CAMERA_REQUEST_CODE = 111;

    private Activity mActivity;

    MainViewModel(Activity activity) {
        mActivity = activity;
    }

    public void openCameraClick(View view) {
        Intent intent = new Intent(mActivity, CameraActivity.class);
        mActivity.startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }
}
