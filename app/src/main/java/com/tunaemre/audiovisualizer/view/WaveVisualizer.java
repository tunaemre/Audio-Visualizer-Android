package com.tunaemre.audiovisualizer.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;

import com.tunaemre.audiovisualizer.R;

public class WaveVisualizer extends View {
    int size = 4;
    private short[] mBytes;
    private float[] mPoints;
    private Rect mRect = new Rect();
    private Config mConfig;

    public WaveVisualizer(Context context) {
        super(context);
        init(context, null);
    }

    public WaveVisualizer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public WaveVisualizer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mBytes = null;
        mConfig = new Config(context, attrs, this);
    }

    public void updateVisualizer(short[] bytes) {
        mBytes = bytes;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mBytes == null) {
            return;
        }
        if (mPoints == null || mPoints.length < mBytes.length * size) {
            mPoints = new float[mBytes.length * size];
        }
        mRect.set(0, 0, getWidth(), getHeight());
        for (int i = 0; i < mBytes.length - 1; i++) {
            mPoints[i * size] = mRect.width() * i / (mBytes.length - 1);
            mPoints[i * size + 1] = mRect.height() / 2 + ((byte) (mBytes[i] + 32768)) * (mRect.height() / 2) / 1000;
            mPoints[i * size + 2] = mRect.width() * (i + 1) / (mBytes.length - 1);
            mPoints[i * size + 3] = mRect.height() / 2 + ((byte) (mBytes[i + 1] + 32768)) * (mRect.height() / 2) / 1000;
        }
        if (mConfig.getColorGradient()) {
            mConfig.resetPaint();
            mConfig.setGradients(this);
        } else {
            mConfig.resetPaint();
        }
        canvas.drawLines(mPoints, mConfig.getPaintWave());
    }

    public Config getConfig() {
        return mConfig;
    }

    public WaveVisualizer setConfig(Config config) {
        this.mConfig = config;
        return this;
    }

    public class Config {

        private int mColor, mStartColor, mEndColor;
        private float mThickness;
        private Boolean mColorGradient = false;
        private WaveVisualizer mWaveVisualizer;

        private Paint mPaint = new Paint();

        public Config(Context context, AttributeSet attrs, WaveVisualizer waveVisualizer) {
            this.mWaveVisualizer = waveVisualizer;

            TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.WaveVisualizer, 0, 0);
            if (attrs != null) {

                mThickness = a.getFloat(R.styleable.WaveVisualizer_waveThickness, 1f);
                mColor = a.getColor(R.styleable.WaveVisualizer_waveColor, Color.parseColor("#691A40"));
                mColorGradient = a.getBoolean(R.styleable.WaveVisualizer_colorGradient, false);
                mStartColor = a.getColor(R.styleable.WaveVisualizer_startColor, Color.parseColor("#93278F"));
                mEndColor = a.getColor(R.styleable.WaveVisualizer_endColor, Color.parseColor("#00A99D"));

                a.recycle();

                mPaint.setStrokeWidth(mThickness);
                mPaint.setAntiAlias(true);
                mPaint.setStyle(Paint.Style.FILL);
                mPaint.setColor(mColor);
                mPaint.setAlpha(255);
            }
        }

        public int getColor() {
            return mColor;
        }

        public Config setColor(int color) {
            this.mColor = color;
            mPaint.setColor(this.mColor);
            mWaveVisualizer.invalidate();
            return this;
        }

        public int getStartColor() {
            return mStartColor;
        }

        public Config setStartColor(int startColor) {
            this.mStartColor = startColor;
            mWaveVisualizer.invalidate();
            return this;
        }

        public int getEndColor() {
            return mEndColor;
        }

        public Config setEndColor(int endColor) {
            this.mEndColor = endColor;
            mWaveVisualizer.invalidate();
            return this;
        }

        public float getThickness() {
            return mThickness;
        }

        public Config setThickness(float thickness) {
            this.mThickness = thickness;
            mPaint.setStrokeWidth(this.mThickness);
            mWaveVisualizer.invalidate();
            return this;
        }

        public Boolean getColorGradient() {
            return mColorGradient;
        }

        public Config setColorGradient(Boolean colorGradient) {
            this.mColorGradient = colorGradient;
            mWaveVisualizer.invalidate();
            return this;
        }

        public Paint getPaintWave() {
            return mPaint;
        }

        public Config setPaintWave(Paint paint) {
            mPaint = paint;
            mWaveVisualizer.invalidate();
            return this;
        }

        public Paint setGradients(WaveVisualizer waveVisualizer) {
            mPaint.setShader(new LinearGradient(0, 0,
                    waveVisualizer.getWidth(), 0,
                    mStartColor, mEndColor, Shader.TileMode.MIRROR));
            waveVisualizer.invalidate();
            return mPaint;
        }

        public Paint resetPaint() {
            mPaint = new Paint();
            mPaint.setStrokeWidth(mThickness);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mColor);
            mPaint.setAlpha(255);
            return mPaint;
        }
    }
}