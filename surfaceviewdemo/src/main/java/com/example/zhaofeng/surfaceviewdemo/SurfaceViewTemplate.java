package com.example.zhaofeng.surfaceviewdemo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by zhaofeng on 2015/7/29.
 */
public class SurfaceViewTemplate extends SurfaceView implements SurfaceHolder.Callback ,Runnable{

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

    public SurfaceViewTemplate(Context context) {
        this(context, null);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);

        //可获得焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        //设置常量
        setKeepScreenOn(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

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

        //不断进行绘制
        while(isRunning){
            draw();
        }
    }

    private void draw(){

        try {

            mCanvas = holder.lockCanvas();
            if(mCanvas != null){
                //draw something



            }
        }catch (Exception e){

        }finally {
            //不管执行结果都要进行释放
            if(mCanvas != null){
                holder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

}
