package com.example.zhaofeng.reveal;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class MyRevealLayout extends LinearLayout implements Runnable {

    /**
     * 画笔工具
     */
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 用户点击处的坐标
     */
    private float mCenterX, mCenterY;

    /**
     * 获取自定义控件MyRevealLayout
     * 在屏幕上的位置
     */
    private int[] mLocation = new int[2];

    /**
     * 重新绘制的时间
     */
    private int INVALIDATE_DURATION = 20;

    /**
     * 被点击的控件的参数
     */
    private int mTargetHeight, mTargetWidth;

    /**
     * mRevealRadius为初始的数值
     * mRevealRadiusGap为每次重新绘制半径增加的值
     * mMaxRadius为绘制的水波纹圆圈最大的半径
     */
    private int mRevealRadius = 0, mRevealRadiusGap, mMaxRadius;

    /**
     * 通过名字就可以看出来了把
     * 在被选中的控件长宽中的最大值和最小值
     */
    private int mMinBetweenWidthAndHeight, mMaxBetweenWidthAndHeight;

    /**
     * 是否被按下
     * 是否需要执行动画
     */
    private boolean mIsPressed;
    private boolean mShouldDoAnimation;

    /**
     * 这个就是在布局文件中被点击的控件了
     */
    private View mTargetView;

    /**
     * 松手的事件分发线程
     */
    private DispatchUpTouchEventRunnable mDispatchUpTouchEventRunnable = new DispatchUpTouchEventRunnable();

    public MyRevealLayout(Context context) {
        super(context);
        init();
    }

    public MyRevealLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public MyRevealLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        setWillNotDraw(false);
        mPaint.setColor(getResources().getColor(R.color.reveal_color));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        this.getLocationOnScreen(mLocation);
    }

    /**
     *
     * @param canvas
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mTargetView == null || !mShouldDoAnimation || mTargetWidth <= 0)
            return;

        if (mRevealRadius > mMinBetweenWidthAndHeight / 2)
            mRevealRadius += mRevealRadiusGap * 4;
        else
            mRevealRadius += mRevealRadiusGap;

        int[] location = new int[2];
        this.getLocationOnScreen(mLocation);
        mTargetView.getLocationOnScreen(location);

        int top = location[1] - mLocation[1];
        int left = location[0] - mLocation[0];
        int right = left + mTargetView.getMeasuredWidth();
        int bottom = top + mTargetView.getMeasuredHeight();

        canvas.save();
        canvas.clipRect(left, top, right, bottom);
        canvas.drawCircle(mCenterX, mCenterY, mRevealRadius, mPaint);
        canvas.restore();

        if (mRevealRadius <= mMaxRadius)
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        else if (!mIsPressed) {
            mShouldDoAnimation = false;
            postInvalidateDelayed(INVALIDATE_DURATION, left, top, right, bottom);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        int action = event.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                View targetView = getTargetView(this, x, y);

                if (targetView != null && targetView.isEnabled()) {
                    mTargetView = targetView;
                    initParametersForChild(event, targetView);
                    postInvalidateDelayed(INVALIDATE_DURATION);
                }
                break;

            case MotionEvent.ACTION_UP:
                mIsPressed = false;
                postInvalidateDelayed(INVALIDATE_DURATION);
                mDispatchUpTouchEventRunnable.event = event;
                postDelayed(mDispatchUpTouchEventRunnable, 100);
                break;

            case MotionEvent.ACTION_CANCEL:
                mIsPressed = false;
                postInvalidateDelayed(INVALIDATE_DURATION);
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    public View getTargetView(View view, int x, int y) {
        View target = null;
        ArrayList<View> views = view.getTouchables();

        for (View child : views)
            if (isTouchPointInView(child, x, y)) {
                target = child;
                break;
            }
        return target;
    }

    public boolean isTouchPointInView(View child, int x, int y) {
        int[] location = new int[2];
        child.getLocationOnScreen(location);

        int top = location[1];
        int left = location[0];
        int right = left + child.getMeasuredWidth();
        int bottom = top + child.getMeasuredHeight();

        if (child.isClickable() && y >= top && y <= bottom && x >= left && x <= right)
            return true;
        else
            return false;
    }

    public void initParametersForChild(MotionEvent event, View view) {
        mCenterX = event.getX();
        mCenterY = event.getY();
        mTargetWidth = view.getMeasuredWidth();
        mTargetHeight = view.getMeasuredHeight();
        mMinBetweenWidthAndHeight = Math.min(mTargetWidth, mTargetHeight);
        mMaxBetweenWidthAndHeight = Math.max(mTargetWidth, mTargetHeight);

        mRevealRadius = 0;
        mRevealRadiusGap = mMinBetweenWidthAndHeight / 20;

        mIsPressed = true;
        mShouldDoAnimation = true;

        int[] location = new int[2];
        view.getLocationOnScreen(location);

        int left = location[0] - mLocation[0];
        int mTransformedCenterX = (int) mCenterX - left;
        mMaxRadius = Math.max(mTransformedCenterX, mTargetWidth - mTransformedCenterX);

        int top = location[1]-mLocation[1];
        int transformedCenterY = (int)mCenterY-top;
        mMaxRadius = Math.max(mMaxRadius,Math.max(transformedCenterY,mTargetHeight-transformedCenterY));
    }

    @Override
    public void run() {
        super.performClick();
    }

    @Override
    public boolean performClick() {
        postDelayed(this, 40);
        return true;
    }

    private class DispatchUpTouchEventRunnable implements Runnable {
        public MotionEvent event;

        @Override
        public void run() {
            if (mTargetView.isEnabled() && mTargetView.isClickable())
                return;

            if (isTouchPointInView(mTargetView, (int) event.getRawX(), (int) event.getRawX()))
                mTargetView.performClick();
        }
    }
}