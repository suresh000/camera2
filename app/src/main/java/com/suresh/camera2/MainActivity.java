package com.suresh.camera2;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.suresh.camera2.databinding.ActivityMainBinding;
import com.suresh.camera2.ui.base.BaseActivity;

public class MainActivity extends BaseActivity {

    private MainViewModel mVm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this,
                R.layout.activity_main);

        mVm = new MainViewModel(this, binding.videoView);
        binding.setVm(mVm);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        mVm.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public Fragment getCurrentFragment() {
        return null;
    }
}