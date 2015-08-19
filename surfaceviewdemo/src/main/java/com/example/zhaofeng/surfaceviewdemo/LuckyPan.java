package com.example.zhaofeng.surfaceviewdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.UnsupportedEncodingException;

/**
 * Created by zhaofeng on 2015/7/29.
 */
public class LuckyPan extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    /**
     * SurfaceView的持有者
     */
    private SurfaceHolder holder;
    /**
     * 画板
     */
    private Canvas mCanvas;
    /**
     * 子线程
     */
    private Thread t;
    /**
     * 线程开关
     */
    private boolean isRunning = false;
    /**
     * 分盘文字
     */
    private String[] mStrs = new String[]{"单反相机", "IPAD", "恭喜发财", "IPHONE", "服装一套", "恭喜发财"};
    /**
     * 分盘图片资源id
     */
    private int[] mImags = new int[]{R.mipmap.danfan, R.mipmap.ipad, R.mipmap.f040, R.mipmap.iphone, R.mipmap.meizi, R.mipmap.f040};

    /**
     * 分盘颜色
     */
    private int[] mColor = new int[]{0xFFFFC300, 0XFFF17E01, 0xFFFFC300, 0XFFF17E01, 0xFFFFC300, 0XFFF17E01, 0xFFFFC300, 0XFFF17E01};

    /**
     * 分盘个数，默认为6
     */
    private int mItemCount = 6;
    /**
     * 将资源装换成Bitmap数组
     */
    private Bitmap[] mImagsBitmap;

    /**
     * 画板的区域
     */
    private RectF mRange = new RectF();
    /**
     * 中心点的位置
     */
    private int mCenter;
    /**
     * 边距，去各个边距的最小值
     * 以paddingLeft为准
     */
    private int mPadding;
    /**
     * 圆形的直径
     */
    private int mRadius;
    /*
     * 图片画笔
     */
    private Paint mArcPaint;
    /**
     * 背景图片
     */
    private Bitmap mBgBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg2);

    /**
     * 文字画笔
     */
    private Paint mTextPaint;
    /**
     * 文字大小
     */
    private float mTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 26, getResources().getDisplayMetrics());


    /**
     * 旋转速度
     */
    private double mSpeed = 0;
    /**
     * 绘制的初始角度
     * volatile为了使各个线程之间是可见的
     */
    private volatile float mStartAngle = 0;
    /**
     * 标记着是否应该结束
     */
    private boolean isShouldEnd;


    public LuckyPan(Context context) {
        this(context, null);
    }

    public LuckyPan(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);

        //设置焦点的可用
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常量
        setKeepScreenOn(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = Math.min(getMeasuredHeight(), getMeasuredWidth());

        mPadding = getPaddingLeft();
        //计算半径
        mRadius = width - mPadding * 2;
        //计算中心位置点
        mCenter = width / 2;

        setMeasuredDimension(width, width);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        //初始化图片资源的画笔
        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setDither(true);

        //初始化文字画笔
        mTextPaint = new Paint();
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(mTextSize);

        //初始化画板的区域
        mRange = new RectF(mPadding, mPadding, mPadding + mRadius, mPadding + mRadius);

        //将图片资源文件，转化成对应的Bitmap
        mImagsBitmap = new Bitmap[mItemCount];

        for (int i = 0; i < mItemCount; i++) {
            mImagsBitmap[i] = BitmapFactory.decodeResource(getResources(), mImags[i]);
        }


        isRunning = true;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        isRunning = false;
    }

    @Override
    public void run() {

        //子线程绘制
        while (isRunning) {
            long start = System.currentTimeMillis();
            draw();
            long end = System.currentTimeMillis();
            if (end - start < 50) {
                try {
                    Thread.sleep(50 - (end - start));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void draw() {

        try {

            mCanvas = holder.lockCanvas();
            if (mCanvas != null) {

                //绘制背景
                drawBg();

                //绘制分盘
                drawPan();

                //draw something
            }
        } catch (Exception e) {

        } finally {
            if (mCanvas != null) {
                holder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    /**
     *转盘开始旋转
     */
    public void luckyStart(int index){
//        int index = 0;

        //计算每个区块的角度
        float angle = 360/mItemCount;

        //计算每个分区对应的起始角度
        float from = 270 - angle*(index+1);
        float end = angle + from;

        //设置指针转过的距离的范围
        float targetFrom = 4*360+from;
        float targetEnd = 4*360+end;

        /**
         * <pre>
         *
         *     计算速度公式
         *     指针速度变化是 v1、v2->0
         *     距离是targetFrom -> targetEnd之间
         *     求解速度 v1、v2,v1、v2是以递减1的等差数列
         *     (v1+0)*(v1+1)/2 = targetFrom; -> v1*v1 + v1 - 2*targetFrom = 0;
         *     (v2+0)*(v2+1)/2 = targetEnd;  -> v2*v2 + v2 - 2*targetEnd = 0;
         *     v1 = (-1+Math.sqrt(1+8*targetFrom))/2;
         *     v2 = (-1+Math.sqrt(1+8*targetEnd))/2;
         *
         * </pre>
         *
         */
        float v1 = (float) ((-1+Math.sqrt(1+8*targetFrom))/2);
        float v2 = (float) ((-1+Math.sqrt(1+8*targetEnd))/2);

        //速度设置成v1~v2之间的随机数
        mSpeed = v1 + Math.random()*(v2 - v1);
//        mSpeed = v2;
        isShouldEnd = false;
    }

    /**
     *转盘停止旋转
     */
    public void luckyEnd(){
        mStartAngle = 0;
        isShouldEnd = true;
    }

    /**
     * 转盘是否还在旋转
     * @return
     */
    public boolean isStart(){
        return mSpeed!=0;
    }

    /**
     * 是否停止旋转
     * @return
     */
    public boolean isShouldEnd(){
        return isShouldEnd;
    }

    /**
     * 绘制背景
     */
    private void drawBg() {
        mCanvas.drawColor(0xFFFFFFFF);
        mCanvas.drawBitmap(mBgBitmap, null, new Rect(mPadding/2, mPadding/2, getMeasuredWidth() - mPadding/2, getMeasuredHeight() - mPadding/2), null);
    }

    /**
     * 绘制分盘
     */
    private void drawPan() {

        float tempAngle = mStartAngle;
        float sweepAngle = 360/mItemCount;
        for (int i=0;i<mItemCount;i++) {

            //设置画笔的颜色
            mArcPaint.setColor(mColor[i]);
            mCanvas.drawArc(mRange, tempAngle, sweepAngle, true, mArcPaint);
            //绘制文本
            drawPanText(tempAngle, sweepAngle, mStrs[i]);
            //绘制图片
            drawPanPicture(tempAngle, sweepAngle, mImagsBitmap[i]);
            tempAngle += sweepAngle;
        }

        mStartAngle += mSpeed;
        //mStartAngle %= 360;

        //如果点击了停止
        if(isShouldEnd){
            mSpeed --;
        }
        if(mSpeed<=0){
            mSpeed = 0;
//            mStartAngle = 0;
        }

    }

    /**
     * 绘制文本
     * 因为文本信息是沿着圆弧来绘制的，所以要是使用Path来实现绘制弧形文字轨迹
     * @param tempAngle
     * @param sweepAngle
     * @param mStr
     */
    private void drawPanText(float tempAngle, float sweepAngle, String mStr) {
        String utfStr = mStr;
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setTextSize(mTextSize);
        Path path = new Path();
        path.addArc(mRange, tempAngle, sweepAngle);
        //文本长度
        float textLengh = mTextPaint.measureText(utfStr);
        //弧度长度，计算公式：弧度*半径 = 弧度长度
        float radLength = (float) ((sweepAngle/180)*Math.PI*mRadius/2);
        //横向偏移量
        int hOffSet = (int) ((radLength-textLengh)/2);
        int vOffSet = mRadius/2/4;//纵向偏移量
        mCanvas.drawTextOnPath(utfStr,path,hOffSet,vOffSet,mTextPaint);
    }

    /**
     * 绘制分盘中的图片
     * 由于图片位于文字和圆心的中点，所以要定位好文字的位置以及图片的大小
     * @param tempAngle
     * @param sweepAngle
     * @param bitmap
     */
    private void drawPanPicture(float tempAngle, float sweepAngle, Bitmap bitmap) {

        //设置图片的大小宽度，防止图片过大，导致不适配的问题
        int imageWidth = mRadius/8;
        //弧度
        float angle = (float) (((tempAngle+sweepAngle/2))*Math.PI/180);
        //图片的中心位置X坐标
        int x = (int) (mRadius/4*Math.cos(angle) + mCenter);
        //图片的中心位置Y坐标
        int y = (int) (mRadius/4*Math.sin(angle) + mCenter);
        //设置图片绘制的区域
        Rect rect = new Rect(x-imageWidth/2,y-imageWidth/2,x+imageWidth/2,y+imageWidth/2);
        mCanvas.drawBitmap(bitmap,null,rect,null);
    }

}