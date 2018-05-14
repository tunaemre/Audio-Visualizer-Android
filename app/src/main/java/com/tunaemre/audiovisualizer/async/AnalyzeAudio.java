package com.tunaemre.audiovisualizer.async;

import android.media.AudioRecord;
import android.support.annotation.NonNull;

import com.google.android.agera.Result;
import com.google.android.agera.Supplier;
import com.tunaemre.audiovisualizer.fft.RealDoubleFFT;

public class AnalyzeAudio implements Supplier<Result<double[]>> {
    private static RealDoubleFFT realDoubleFFT;
    private int mBufferSize;
    private AudioRecord mAudioRecord;

    public AnalyzeAudio(int bufferSize, AudioRecord audioRecord) {
        this.mBufferSize = bufferSize;
        this.mAudioRecord = audioRecord;

        realDoubleFFT =  new RealDoubleFFT(mBufferSize);
    }

    @NonNull
    @Override
    public Result<double[]> get() {
        try {
            Thread.sleep(10);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        short[] buffer = new short[mBufferSize];
        double[] fftBuffer = new double[mBufferSize];

        int bufferReadResult;

        if (mAudioRecord != null) {
            bufferReadResult = mAudioRecord.read(buffer, 0, mBufferSize);

            for (int i = 0; i < Math.min(mBufferSize, bufferReadResult); i++) {
                fftBuffer[i] = (double) buffer[i] / 32768.0; // signed 16 bit short
            }

            realDoubleFFT.forwardTransform(fftBuffer);
        }

        return Result.success(fftBuffer);
    }
}
