package com.la.mymedia.player;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class XPlay extends GLSurfaceView implements SurfaceHolder.Callback, GLSurfaceView.Renderer, View.OnClickListener {

    static {
        System.loadLibrary("XMedia");
    }
// /storage/4925-382B/1234.mp4"
    private String url;
    public XPlay(Context context, AttributeSet attrs) {
        super( context, attrs );

        //android 8.0 需要设置
        setRenderer( this );
        setOnClickListener( this );
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //初始化opengl egl 显示
        InitView(holder.getSurface());

        //只有在绘制数据改变时才绘制view，可以防止GLSurfaceView帧重绘
        //setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder var1, int var2, int var3, int var4)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder var1)
    {

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {
    // 可以做特效处理
    }

    public void setPlayUrl(String url){
        // 如果surface没有初始化完成，那么会直接崩溃
        this.url = url;
        Open(url);
    }

    @Override
    public void onClick(View view) {
        // 事件被拦截就无法执行
        // PlayOrPause();
    }
    // 传入播放Surface
    public native void InitView(Object surface);
    // 打开
    public native void Open(String url);
    // 播放或暂停
    public native void PlayOrPause();
    // 设置进度
    public native void Seek(double pos);
    // 获取播放位置，用于设置进度条
    public native double PlayPos();


}
