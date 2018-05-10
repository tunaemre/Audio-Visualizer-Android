package com.tunaemre.audiovisualizer.async;

import com.google.android.agera.BaseObservable;
import com.tunaemre.audiovisualizer.UpdateListener;

public class UpdateObservable extends BaseObservable implements UpdateListener {

    @Override
    public void postUpdate() {
        dispatchUpdate();
    }
}
