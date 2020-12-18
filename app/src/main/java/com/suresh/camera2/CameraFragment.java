package com.suresh.camera2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.suresh.camera2.camera.CameraVideoFragment;
import com.suresh.camera2.databinding.FragmentCameraBinding;

public class CameraFragment extends CameraVideoFragment implements View.OnClickListener {

    private Activity mActivity;
    private String mOutputFilePath;
    private CountDownTimer timer;

    private FragmentCameraBinding mBinding;
    private CameraViewModel mVm;

    public CameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     */
    public static CameraFragment newInstance() {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_camera, container, false);

        mVm = new CameraViewModel();
        mBinding.setVm(mVm);

        mBinding.mRecordVideo.setOnClickListener(this);

        return mBinding.getRoot();
    }

    @Override
    public int getTextureResource() {
        return R.id.mTextureView;
    }

    @Override
    protected void setUp(View view) {

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.mRecordVideo) {
            /*
              If media is not recoding then start recording else stop recording
             */
            if (mIsRecordingVideo) {
                stopCamera();
            } else {
                startRecordingVideo();
                mBinding.mRecordVideo.setImageResource(R.drawable.ic_stop);
                //Receive out put file here
                mOutputFilePath = getCurrentFile().getAbsolutePath();

                mVm.isVisibleTimer.set(true);
                timer = new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        mVm.updateTimer.set("" + millisUntilFinished / 1000 + " sec");
                    }

                    public void onFinish() {
                        stopCamera();
                    }
                }.start();
            }
        }
    }

    private void stopCamera() {
        mVm.isVisibleTimer.set(false);
        if (timer != null) {
            timer.cancel();
        }
        try {
            stopRecordingVideo();
            finishCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void finishCamera() {
        Intent intent = new Intent();
        intent.putExtra(ConstantValues.PATH, mOutputFilePath);
        mActivity.setResult(Activity.RESULT_OK, intent);
        mActivity.finish();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}