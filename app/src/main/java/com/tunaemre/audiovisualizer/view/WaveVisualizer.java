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
import com.tunaemre.audiovisualizer.math.ArrayScaler;

public class WaveVisualizer extends View {
    private double[] mDoubles;
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
        mDoubles = null;
        mConfig = new Config(context, attrs, this);
    }

    public void updateVisualizer(double[] doubles) {
        if (doubles.length > 512) {
            mDoubles = ArrayScaler.getInstance(512).scale(doubles);
        }
        else
            mDoubles = doubles;

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mDoubles == null)
            return;

        mRect.set(0, 0, getWidth(), getHeight());

        for (int i = 0; i < mDoubles.length; i++) {
            int x = mRect.width() * (i + 1) / (mDoubles.length - 1);
            int maxY = mRect.height();
            int miny = (int) (maxY - (mDoubles[i] * maxY / 50));

            canvas.drawLine(x, miny, x, maxY, mConfig.getPaintWave());
        }
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

        public Paint resetPaint() {
            mPaint = new Paint();
            mPaint.setStrokeWidth(mThickness);
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(mColor);
            mPaint.setAlpha(255);
            if (mColorGradient)
                mPaint.setShader(new LinearGradient(0, 0, mWaveVisualizer.getWidth(), 0, mStartColor, mEndColor, Shader.TileMode.MIRROR));
            return mPaint;
        }
    }
}