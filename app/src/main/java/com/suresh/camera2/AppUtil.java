package com.suresh.camera2;

import android.os.Environment;

import java.io.File;

public interface AppUtil {

    String VID_CAPTURE = "VID_CAPTURE";
    String VID_FINAL = "VID_FINAL";

    /**
     * Create directory and return file
     * returning video file
     */
    static File getOutputMediaFile(String name) {
        // External sdcard file location
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(),
                ConstantValues.VIDEO_DIRECTORY_NAME);
        // Create storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + name + ".mp4");

        if (mediaFile.exists()) {
            mediaFile.delete();
        }

        return mediaFile;
    }

}
