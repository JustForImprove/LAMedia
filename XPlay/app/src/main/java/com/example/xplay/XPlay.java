package com.example.xplay;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ProgressBar;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
public class XPlay extends GLSurfaceView implements SurfaceHolder.Callback, GLSurfaceView.Renderer {
    static {
        System.loadLibrary("native-lib");

    }

    public XPlay(Context context, AttributeSet attrs) {
        super( context, attrs );
    }
    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        //初始化opengl egl 显示

        InitView(holder.getSurface());
        Open("/storage/4925-382B/a.mp4");
        setRenderer(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder var1, int var2, int var3, int var4)
    {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder var1)
    {

    }
    public native void InitView(Object surface);
    public native void Open(String url);

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int i, int i1) {

    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }
}
