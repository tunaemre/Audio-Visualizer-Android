package com.tunaemre.audiovisualizer.async;

import android.media.AudioRecord;
import android.support.annotation.NonNull;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;

public class AnalyzeAudio implements Supplier<Result<short[]>> {

    private int mBufferSize;
    private AudioRecord mAudioRecord;

    public  AnalyzeAudio(int bufferSize, AudioRecord audioRecord) {
        this.mBufferSize = bufferSize;
        this.mAudioRecord = audioRecord;
    }

    @NonNull
    @Override
    public Result<short[]> get() {
        try {
            Thread.sleep(10);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        short[] buffer = new short[mBufferSize];

        int bufferReadResult = 1;

        if (mAudioRecord != null) {
            bufferReadResult = mAudioRecord.read(buffer, 0, mBufferSize);
            /*for (int i = 0; i < mBufferSize && i < bufferReadResult; i++) {
                toTransform[i] = (double) buffer[i] / 32768.0; // signed
                // 16
            }*/
        }

        return Result.success(buffer);
    }
}
