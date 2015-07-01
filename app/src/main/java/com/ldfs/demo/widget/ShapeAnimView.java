package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.ldfs.demo.draw.ShapeConfig;
import com.ldfs.demo.draw.ShapeSet;

/**
 * Created by cz on 15/6/23.
 */
public class ShapeAnimView extends View {
    private ShapeConfig mLineConfig;
    private ShapeConfig mRectangleConfig;
    private ShapeConfig mArcConfig;
    private ShapeConfig mCricleConfig;
    private ShapeConfig mTextConfig;
    private ShapeConfig mWave1Config;
    private ShapeConfig mWave2Config;
    private ShapeConfig mWave3Config;

    public ShapeAnimView(Context context) {
        super(context);
    }

    public ShapeAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLineConfig = new ShapeConfig.Builder().setTargetColor(Color.RED).setDuration(1 * 1000).build();
        mRectangleConfig = new ShapeConfig.Builder().setTargetColor(Color.BLUE).setDuration(1 * 1000).setRepeatMode(ShapeConfig.REVERSE).build();
        mArcConfig = new ShapeConfig.Builder().setTargetColor(Color.GREEN).setDuration(1 * 1000).build();
        mCricleConfig = new ShapeConfig.Builder().setTargetColor(Color.YELLOW).setDuration(1 * 1000).setDelayDuration(1000).build();
        mTextConfig = new ShapeConfig.Builder().setColor(Color.RED).setDuration(2 * 1000).build();
        mWave1Config = new ShapeConfig.Builder().setColor(Color.YELLOW)
                .setTargetColor(Color.YELLOW)
                .setDuration(3 * 1000)
                .setAlpha(0x88)
                .setTargetAlpha(0x00)
                .setRepeatMode(ShapeConfig.RESTART)
                .setRepeatCount(-1).build();
        mWave2Config = mWave1Config.clone();
        mWave2Config.setDelayDuration(100);
        mWave3Config = new ShapeConfig.Builder().setTargetColor(Color.RED).setDuration(400).setAlpha(0).setStyle(Paint.Style.STROKE).setTargetAlpha(0xFF).setRepeatMode(ShapeConfig.RESTART).setRepeatCount(-1).build();
    }

    public ShapeAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        ShapeSet.target(this).line(100, 100, 100, 1000, mLineConfig).
                afterCircle(300, 100, 50, 100, mCricleConfig).
                afterRanctangle(500, 100, 600, 300, mRectangleConfig).
                afterArc(700, 100, 800, 500, 0, 180, true, mArcConfig).
                text("水波纹效果", 100, height / 2, 30, 40, mTextConfig).
                circle(width / 2, height / 2, 200, mWave1Config).
                circle(width / 2, height / 2, 180, 200, mWave2Config).
                circle(width / 2, height / 2, 180, 200, mWave3Config).darw(canvas);

    }
}
