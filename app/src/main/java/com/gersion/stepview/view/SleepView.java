package com.gersion.stepview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @作者 Gersy
 * @版本
 * @包名 com.gersion.stepview.view
 * @待完成
 * @创建时间 2016/11/18
 * @功能描述 TODO
 * @更新人 $
 * @更新时间 $
 * @更新版本 $
 */
public class SleepView extends View {

    private Paint mPaint;

    public SleepView(Context context) {
        this(context,null);
    }

    public SleepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(90);
        mPaint.setFilterBitmap(true);
        mPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        RectF rect = new RectF();
        rect.left = 100;
        rect.right = 200;
        rect.top = 100;
        rect.bottom = 400;
        canvas.drawRect(rect,mPaint);
    }
}
