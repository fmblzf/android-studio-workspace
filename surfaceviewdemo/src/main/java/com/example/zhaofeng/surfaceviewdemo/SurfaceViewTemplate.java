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
     * SurfaceView�ĳ�����
     */
    private SurfaceHolder holder;
    /**
     * ����
     */
    private Canvas mCanvas;
    /**
     * ���߳�
     */
    private Thread t;
    /**
     * �߳̿���
     */
    private boolean isRunning = false;

    public SurfaceViewTemplate(Context context) {
        this(context, null);
    }

    public SurfaceViewTemplate(Context context, AttributeSet attrs) {
        super(context, attrs);
        holder = getHolder();
        holder.addCallback(this);

        //�ɻ�ý���
        setFocusable(true);
        setFocusableInTouchMode(true);
        //���ó���
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

        //���Ͻ��л���
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
            //����ִ�н����Ҫ�����ͷ�
            if(mCanvas != null){
                holder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

}
