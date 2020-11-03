package com.la.customview.SmartViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.la.customview.R;


/**
 * Created by 10415 on 2018/1/31.
 */

public class SmartViewPager extends FrameLayout {

    int[] images = {R.drawable.f1,R.drawable.f2,R.drawable.f3};

    ImageView imageView[] = new ImageView[images.length];

    Context context;

    public SmartViewPager(@NonNull Context context) {

        super(context);
        this.context = context;
        init();
    }




    public SmartViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        for (int i = 0; i < images.length;i++){
             imageView[i] = new ImageView(context);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),images[i]);
            imageView[i].setImageBitmap(bitmap);
            this.addView(imageView[i]);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for (int i = 0; i < images.length; i++) {
            getChildAt(i).layout(i * getWidth(), 0, (i + 1) * getWidth(), getHeight());
        }
    }


    int startX = 0;
    int endX = 0;
    int distanceX = 0;
    int i = 0;

    int startTime;
    int courseTime = 500;
    int scrollDistanceX = i * getWidth() - startX;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

       gestureDetector.onTouchEvent(event);
       requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()){

            case MotionEvent.ACTION_DOWN:
                startX = (int) event.getX();
                break;
            case MotionEvent.ACTION_UP:
                endX = (int) event.getX();
                distanceX = (int) (startX - endX);
                startX = getScrollX();
                if (distanceX > getWidth()/2){
                    i++;
                }else if (distanceX < -getWidth()/2){
                    i--;
                }
                if (i < 0){
                    i = 0;
                }else if(i >= images.length){
                                                                // 重新回去
                    i = 0;
                }

                scrollDistanceX = i * getWidth() - startX;
                startTime = (int) SystemClock.uptimeMillis();
                isFinsh = false;
                invalidate();
                break;
        }

        return true;
    }

GestureDetector gestureDetector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        scrollBy((int) distanceX,0);
        return super.onScroll(e1, e2, distanceX, distanceY);
    }
});

    boolean isFinsh = false;
   int curX = 0;
    private void scrollToi() {

        curX = 0;
        int endTime = (int) SystemClock.uptimeMillis();
        curX =  (startX + scrollDistanceX * (endTime - startTime)/courseTime);
        if (courseTime <= endTime - startTime) {
            isFinsh = true;
            curX = i * getWidth();
        }


    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (isFinsh == false){
            // isFinsh = false;
            scrollToi();
            scrollTo(curX,0);
            invalidate();
        }


    }
}
