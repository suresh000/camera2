package com.suresh.camera2;

import androidx.databinding.ObservableBoolean;
import androidx.databinding.ObservableField;

public class CameraViewModel {

    public final ObservableField<String> updateTimer = new ObservableField<>();
    public final ObservableBoolean isVisibleTimer = new ObservableBoolean(false);

}
