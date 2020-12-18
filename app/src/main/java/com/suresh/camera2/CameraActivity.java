package com.suresh.camera2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.suresh.camera2.ui.base.BaseActivity;

public class CameraActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_camera);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        if (savedInstanceState == null) {
            replaceFragment(CameraFragment.newInstance());
        }
    }

    @Override
    public Fragment getCurrentFragment() {
        FragmentManager manager = getSupportFragmentManager();
        return manager.findFragmentById(R.id.fragmentContainer);
    }
}