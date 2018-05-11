package com.tunaemre.audiovisualizer;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.agera.Repositories;
import com.google.android.agera.Repository;
import com.google.android.agera.Result;
import com.google.android.agera.Updatable;
import com.tunaemre.audiovisualizer.async.AnalyzeAudio;
import com.tunaemre.audiovisualizer.async.UpdateObservable;
import com.tunaemre.audiovisualizer.view.WaveVisualizer;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newSingleThreadExecutor;

public class MainFragment extends Fragment implements Updatable {

    private AudioRecord mAudioRecord;
    private int mBufferSize;
    private WaveVisualizer mWaveVisualizer;

    private UpdateObservable mUpdateObservable = new UpdateObservable();

    private ExecutorService mExecutor;
    private Repository<Result<double[]>> mUpdateRepository;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mWaveVisualizer = (WaveVisualizer) inflater.inflate(R.layout.fragment_main, container, false);

        prepareFragment();

        return mWaveVisualizer;
    }

    @Override
    public void onResume() {
        super.onResume();
        mUpdateRepository.addUpdatable(this);
        mAudioRecord.startRecording();

        mUpdateObservable.postUpdate();
    }

    @Override
    public void onPause() {
        super.onPause();
        mUpdateRepository.removeUpdatable(this);
        mAudioRecord.stop();
    }

    private void prepareFragment() {
        mBufferSize = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        Log.e("AudioRecord", "getMinBufferSize:" + mBufferSize);
        mAudioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, mBufferSize);

        mExecutor = newSingleThreadExecutor();

        mUpdateRepository = Repositories
                .repositoryWithInitialValue(Result.<double[]>absent())
                .observe(mUpdateObservable)
                .onUpdatesPerLoop()
                .goTo(mExecutor)
                .thenGetFrom(new AnalyzeAudio(mBufferSize, mAudioRecord))
                .compile();
    }

    @Override
    public void update() {
        if (mUpdateRepository.get().succeeded())
            mWaveVisualizer.updateVisualizer(mUpdateRepository.get().get());

        mUpdateObservable.postUpdate();
    }
}
