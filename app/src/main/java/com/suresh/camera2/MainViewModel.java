package com.suresh.camera2;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.databinding.ObservableBoolean;

import com.coremedia.iso.IsoFile;
import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.TimeToSampleBox;
import com.coremedia.iso.boxes.TrackBox;
import com.googlecode.mp4parser.DataSource;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Mp4TrackImpl;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainViewModel {

    public final ObservableBoolean isVisibleVideo = new ObservableBoolean(false);

    private Activity mActivity;
    private VideoView mVideoView;
    private String mOutputFilePath;

    MainViewModel(Activity activity, VideoView videoView) {
        mActivity = activity;
        mVideoView = videoView;
        mVideoView.setOnPreparedListener(mp -> mp.setLooping(true));
    }

    public void openCameraClick(View view) {
        Intent intent = new Intent(mActivity, CameraActivity.class);
        mActivity.startActivityForResult(intent, ConstantValues.CAMERA_REQUEST_CODE);
    }

    void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == ConstantValues.CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    mOutputFilePath = data.getStringExtra(ConstantValues.PATH);
                    if (!TextUtils.isEmpty(mOutputFilePath)) {
                        isVisibleVideo.set(true);
                        try {
                            setMediaForRecordVideo();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private void setMediaForRecordVideo() throws IOException {
        mOutputFilePath = parseVideo(mOutputFilePath);
        // Set media controller
        mVideoView.setMediaController(new MediaController(mActivity));
        mVideoView.requestFocus();
        mVideoView.setVideoPath(mOutputFilePath);
        mVideoView.seekTo(100);
        mVideoView.setOnCompletionListener(mp -> {
            // Reset player
            //mVideoView.setVisibility(View.GONE);
        });
        mVideoView.start();
    }

    private String parseVideo(String mFilePath) throws IOException {
        DataSource channel = new FileDataSourceImpl(mFilePath);
        IsoFile isoFile = new IsoFile(channel);
        List<TrackBox> trackBoxes = isoFile.getMovieBox().getBoxes(TrackBox.class);
        boolean isError = false;
        for (TrackBox trackBox : trackBoxes) {
            TimeToSampleBox.Entry firstEntry = trackBox.getMediaBox().getMediaInformationBox().getSampleTableBox().getTimeToSampleBox().getEntries().get(0);
            // Detect if first sample is a problem and fix it in isoFile
            // This is a hack. The audio deltas are 1024 for my files, and video deltas about 3000
            // 10000 seems sufficient since for 30 fps the normal delta is about 3000
            if (firstEntry.getDelta() > 10000) {
                isError = true;
                firstEntry.setDelta(3000);
            }
        }
        File file = getOutputMediaFile();
        String filePath = file.getAbsolutePath();
        if (isError) {
            Movie movie = new Movie();
            for (TrackBox trackBox : trackBoxes) {
                movie.addTrack(new Mp4TrackImpl(channel.toString() + "[" + trackBox.getTrackHeaderBox().getTrackId() + "]", trackBox));
            }
            movie.setMatrix(isoFile.getMovieBox().getMovieHeaderBox().getMatrix());
            Container out = new DefaultMp4Builder().build(movie);

            //delete file first!
            FileChannel fc = new RandomAccessFile(filePath, "rw").getChannel();
            out.writeContainer(fc);
            fc.close();
            return filePath;
        }
        return mFilePath;
    }

    /**
     * Create directory and return file
     * returning video file
     */
    private File getOutputMediaFile() {
        // External sdcard file location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
                ConstantValues.VIDEO_DIRECTORY_NAME);
        // Create storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "VID_" + timeStamp + ".mp4");
        return mediaFile;
    }
}
