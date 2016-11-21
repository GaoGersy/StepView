package com.gersion.stepview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.Log;
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

    boolean isRealy = false;
    boolean isLoadingResult = false;
    private Paint mBlackTextPaint;
    private Paint mPaint;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;
    private Paint mDotPaint;
    private Paint mArcPaint;
    private float mDegree = 0;
    private int mMaxProgress;
    private int mProgress;
    private Paint mTextPaint;
    private String mDistance;
    private int mLineDistance;
    private int mStrokeWidth;
    private int mBigTextSize;
    private int mSmallTextSize;
    private int mDotSize;
    private int mColor;
    private ValueAnimator secondAnim;
    private float rotateSecondPointer;
    private ValueAnimator resultAnim;
    private float rotateDegree;

    public StepView(Context context) {
        this(context, null);
    }

    public StepView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
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
        startLoading();

    }

    private void initData(Context context) {
        mStrokeWidth = dip2px(context, 1);
        mLineDistance = dip2px(context, 10);
        mBigTextSize = dip2px(context, 50);
        mSmallTextSize = dip2px(context, 15);
        mDotSize = dip2px(context, 5);
        mColor = Color.parseColor("#ffffff");
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setFilterBitmap(true);
        mPaint.setAlpha(120);
    }

    private void initDotPaint() {
        mDotPaint = new Paint();
        mDotPaint.setAntiAlias(true);
        mDotPaint.setColor(mColor);
    }

    private void initArcPaint() {
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(mColor);
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
        mBlackTextPaint.setAlpha(150);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec) / 2;
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mCenterX = getWidth() / 2;
        mCenterY = getHeight() / 2;
        mRadius = getWidth() / 3;

        int startX = mCenterX;
        int startY = mCenterY - mRadius - mLineDistance;
        //绘制最外层的圆环
        canvas.drawCircle(mCenterX, mCenterY, mRadius + mLineDistance, mPaint);
        //绘制最中间的圆环
//        canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);

        //绘制最里层的圆环
//        canvas.drawCircle(mCenterX, mCenterY, mRadius - mLineDistance - mStrokeWidth, mPaint);

        //绘制中间的文字
        String stepText = mProgress + "";
//        Log.d("aa",stepText);
        float bottom = getBottomPoint(mTextPaint);
        canvas.drawText(stepText, mCenterX, mCenterY + bottom, mTextPaint);

        //绘制中间的文字
        String textStep = "步";
        float start = getTextWidth(mTextPaint, stepText);
        canvas.drawText(textStep, mCenterX + start + mDotSize * 2, mCenterY + bottom, mBlackTextPaint);

        mDistance = getStepDistance(mProgress);

        canvas.drawText(mDistance, mCenterX, mCenterY + bottom + mLineDistance * 2, mBlackTextPaint);
        if (isRealy) {
            //绘制刻度
            drawnLine(canvas);
            //绘制进度
            drawProgress(canvas);
            canvas.drawCircle(startX, startY, mDotSize, mDotPaint);

        } else {
            //绘制小白点
            canvas.rotate(rotateSecondPointer, mCenterX, mCenterY);
            canvas.drawCircle(startX, startY, mDotSize, mDotPaint);
        }
    }

    //获取字符的高度
    private float getBottomPoint(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return fontMetrics.bottom;
    }

    //计算行走的里程及消耗的卡路里
    private String getStepDistance(int progress) {
        float distance = progress / 1420f;
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        String format = decimalFormat.format(distance);
        int carol = (int) (distance * 32.3f);
        return format + "公里 | " + carol + "千卡";
    }

    //获取字符相对中心向右偏移的量
    private float getTextWidth(Paint paint, String text) {
        return paint.measureText(text, 0, text.length()) / 2;
    }

    private void drawProgress(Canvas canvas) {
        int offest = mLineDistance / 2 + mStrokeWidth / 2;
        RectF oval = new RectF();
        oval.left = mCenterX - mRadius + offest;
        oval.top = mCenterY - mRadius + offest;
        oval.right = mCenterX + mRadius - offest;
        oval.bottom = mCenterY + mRadius - offest;
        canvas.drawArc(oval, -90,isLoadingResult?mDegree:rotateDegree , false, mArcPaint);
    }

    /**
     * 画刻度
     */
    private void drawnLine(Canvas canvas) {
        for (int i = 0; i < 360; i++) {
            int startX = mCenterX;
            int startY = mCenterY - mRadius + mStrokeWidth / 2;
            int stopX = mCenterX;
            int stopY = startY + mLineDistance;
            canvas.drawLine(startX, startY, stopX, stopY, mPaint);
            //如果数字是正数， 那么表示向右旋转，在这个地方开始作画
            canvas.rotate(1f, mCenterX, mCenterY);
        }
    }

    //目标完成进度动画
    private void resultLoading() {
        final float startDegree = 0f;
        float endDegree = getDegree(mProgress);
        resultAnim = ValueAnimator.ofFloat(startDegree,endDegree);
        resultAnim.setDuration(1000);
        resultAnim.setInterpolator(new LinearOutSlowInInterpolator());
        resultAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float newValue = (float) animation.getAnimatedValue();
                rotateDegree = newValue;
                invalidate();
            }
        });
        resultAnim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isLoadingResult = true;
            }
        });
        resultAnim.start();
    }

    //加载手环数据动画
    private void startLoading() {
        final float startDegree = 0f;
        final float endDegree = 360f;
        secondAnim = ValueAnimator.ofFloat(startDegree, endDegree);
        secondAnim.setDuration(1000);
        secondAnim.setInterpolator(new LinearOutSlowInInterpolator());
        secondAnim.setRepeatMode(ValueAnimator.RESTART);
        secondAnim.setRepeatCount(ValueAnimator.INFINITE);
        secondAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private float lastDrawValue = 0;
            private float drawInterval = 0.1f;

            private float lastUpdatePointerValue = 0;
            private float updatePointerInterval = 360 / 60 * 5;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float newValue = (float) animation.getAnimatedValue();

                // Check if it is the time to update pointer position
                float increasedPointerValue = newValue - lastUpdatePointerValue;
                if (increasedPointerValue < 0) {
                    increasedPointerValue = endDegree + increasedPointerValue;
                }
                if (increasedPointerValue >= updatePointerInterval) {
                    lastUpdatePointerValue = newValue;
//                    updateTimePointer();
                }

                // Check if it is the time to invalidate
                float increasedDrawValue = newValue - lastDrawValue;
                if (increasedDrawValue < 0) {
                    increasedDrawValue = endDegree + increasedDrawValue;
                }
                if (increasedDrawValue >= drawInterval) {
                    lastDrawValue = newValue;
                    rotateSecondPointer += increasedDrawValue;
                    Log.d("aa",rotateSecondPointer+"");
                    invalidate();
                }
            }
        });
        secondAnim.start();
    }

    private int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setProgress(int progress) {
        getDegree(progress);
        invalidate();
    }

    private void setProgress(float progress){
        mDegree = progress;
        invalidate();
    }

    private float getDegree(int progress) {
        mProgress = progress;
        float precent = mProgress / (mMaxProgress + 0f);
        mDegree = 360 * precent;
        return mDegree;
    }

    public void setMaxProgress(int max) {
        mMaxProgress = max;
        setProgress(mProgress);
        invalidate();
    }

    public void setLineDistance(int distance) {
        mLineDistance = distance;
        initArcPaint();
        invalidate();
    }

    public void setStrokeWidth(int strokeWidth) {
        mStrokeWidth = strokeWidth;
        initArcPaint();
        initPaint();
        invalidate();
    }

    public void setBigTextSize(int size) {
        mBigTextSize = size;
        initTextPaint();
        invalidate();
    }

    public void setSmallTextSize(int size) {
        mSmallTextSize = size;
        initBlackTextPaint();
        invalidate();
    }

    public void setDotSize(int dotSize) {
        mDotSize = dotSize;
        invalidate();
    }

    public void stopAnimator(int progress) {
        mProgress = progress;
        secondAnim.cancel();
        isRealy = true;
        invalidate();
        resultLoading();
    }

    public void setColor(int color) {
        mColor = color;
        initPaint();
        initDotPaint();
        initArcPaint();
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
//        super.onWindowVisibilityChanged(visibility);
        if (visibility == INVISIBLE){
            Log.d("aa",visibility+"adsfasdfasdfasdf");
            resultAnim.cancel();
            secondAnim.cancel();
        }
    }

    @Override
    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (!gainFocus){
            resultAnim.cancel();
            secondAnim.cancel();
        }
    }
}
