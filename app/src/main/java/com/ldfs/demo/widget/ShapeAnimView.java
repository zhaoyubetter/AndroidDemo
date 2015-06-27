package com.ldfs.demo.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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


    public ShapeAnimView(Context context) {
        super(context);
    }

    public ShapeAnimView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mLineConfig = new ShapeConfig.Builder().setTargetColor(Color.RED).setDuration(1*1000).build();
        mRectangleConfig = new ShapeConfig.Builder().setTargetColor(Color.BLUE).setDuration(2*1000).build();
        mArcConfig = new ShapeConfig.Builder().setTargetColor(Color.GREEN).setDuration(3*1000).build();
        mCricleConfig = new ShapeConfig.Builder().setTargetColor(Color.YELLOW).setDuration(5*1000).build();
    }

    public ShapeAnimView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ShapeSet.target(this).line(100,100,100,1000,mLineConfig).
                afterCircle(200,100,50,100,mCricleConfig).
                ranctangle(400,100,600,300,mRectangleConfig).
                afterArc(600,100,1000,500,0,180,true,mArcConfig).darw(canvas);
    }

    public void startAnim() {
    }
}
