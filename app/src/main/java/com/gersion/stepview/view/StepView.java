package com.gersion.stepview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.text.DecimalFormat;

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
public class StepView extends View {

    private Paint mBlackTextPaint;
    private Paint mPaint;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private Paint mDotPaint;
    private Paint mArcPaint;
    private float mDegree;
    private int mMaxProgress;
    private int mProgress;
    private Paint mTextPaint;
    private String mDistance;
    private int mLineDistance;
    private int mStrokeWidth;
    private int mBigTextSize;
    private int mSmallTextSize;
    private int mDotSize;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mStrokeWidth = dip2px(context,1);
        mLineDistance = dip2px(context,10);
        mBigTextSize = dip2px(context,50);
        mSmallTextSize = dip2px(context,15);
        mDotSize = dip2px(context,5);
        //初始化线条画笔
        initPaint();

        //初始化小圆点画笔
        initDotPaint();

        //初始化进度画笔
        initArcPaint();

        //初始化步数文字画笔
        initTextPaint();

        //初始化卡路里文字画笔
        initBlackTextPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffff"));
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setFilterBitmap(true);
        mPaint.setAlpha(90);
    }

    private void initDotPaint() {
        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setColor(Color.parseColor("#ffffff"));
    }

    private void initArcPaint() {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(Color.parseColor("#ffffff"));
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(mLineDistance + mStrokeWidth);
    }

    private void initTextPaint() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mBigTextSize);
        mTextPaint.setColor(Color.WHITE);
    }

    private void initBlackTextPaint() {
        mBlackTextPaint = new Paint();
        mBlackTextPaint.setAntiAlias(true);
        mBlackTextPaint.setTextAlign(Paint.Align.CENTER);
        mBlackTextPaint.setTextSize(mSmallTextSize);
        mBlackTextPaint.setColor(Color.WHITE);
        mBlackTextPaint.setAlpha(90);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec)/2;
        int measureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        mRadius = getWidth() / 3;

        int startX = mCenterX;
        int startY = mCenterY - mRadius - mLineDistance;

        //绘制小白点
        canvas.drawCircle(startX, startY, mDotSize, mDotPaint);
        //绘制最外层的圆环
        canvas.drawCircle(mCenterX, mCenterY, mRadius + mLineDistance, mPaint);
        //绘制最中间的圆环
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
        //绘制刻度
        drawnLine(canvas);
        //绘制最里层的圆环
        canvas.drawCircle(mCenterX, mCenterY, mRadius - mLineDistance - mStrokeWidth, mPaint);

        //绘制进度
        drawProgress(canvas);

        //绘制中间的文字
        String stepText = mProgress + "";
        canvas.drawText(stepText, mCenterX, mCenterY, mTextPaint);

        //绘制中间的文字
        String textStep = "步";
        float start = getTextWidth(mTextPaint, stepText);
        canvas.drawText(textStep, mCenterX + start + mLineDistance, mCenterY, mBlackTextPaint);

        mDistance = getStepDistance(mProgress);
        float bottom = getBottomPoint(mTextPaint);
        canvas.drawText(mDistance, mCenterX, mCenterY + bottom + mLineDistance, mBlackTextPaint);
    }

    //获取字符的高度
    private float getBottomPoint(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.bottom;
    }

    //计算行走的里程及消耗的卡路里
    private String getStepDistance(int progress){
        float distance = progress / 1420f;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String format = decimalFormat.format(distance);
        int carol = (int) (distance * 32.3f);
        return format+"公里 | "+carol+"千卡";
    }

    //获取字符相对中心向右偏移的量
    private float getTextWidth(Paint paint, String text) {
        return paint.measureText(text, 0, text.length())/2;
    }

    private void drawProgress(Canvas canvas) {
        int offest = mLineDistance / 2+mStrokeWidth/2;
        RectF oval = new RectF();
        oval.left = mCenterX - mRadius + offest;                              //左边
        oval.top = mCenterY - mRadius + offest;                                   //上边
        oval.right = mCenterX + mRadius - offest;                             //右边
        oval.bottom = mCenterY + mRadius - offest;                                //下边
        canvas.drawArc(oval, -90, mDegree, false, mArcPaint);
    }

    /**
     * 画刻度
     */
    private void drawnLine(Canvas canvas) {
        for (int i = 0; i < 360; i++) {
            int startX = mCenterX;
            int startY = mCenterY - mRadius+mStrokeWidth/2;
            int stopX = mCenterX;
            int stopY = startY + mLineDistance;
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
            //如果数字是正数， 那么表示向右旋转，在这个地方开始作画
            canvas.rotate(1f, mCenterX, mCenterY);
        }
    }

    public void setProgress(int progress) {
        mProgress = progress;
        float precent = mProgress / (mMaxProgress + 0f);
        mDegree = 360 * precent;
    }

    public void setMaxProgress(int max) {
        mMaxProgress = max;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
