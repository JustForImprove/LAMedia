package com.la.mymedia.customview.SmartItemDecoration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SmartItemDecoration extends RecyclerView.ItemDecoration {

    Context context;
    int gap = 3;
    public SmartItemDecoration(Context context) {
        this.context = context;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int i = state.getItemCount();
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        int j = i/ 2;
        i = i % 2;
        if(i != 0) {
            c.drawRect(0, 0, parent.getRight(), parent.getBottom() / (j + 1) * j + gap, p);
            c.drawRect(0, parent.getBottom() / (j + 1) * j , parent.getRight()/ 2 * i + gap, parent.getBottom() + gap, p);
        }else{
            c.drawRect(0, 0, parent.getRight(), parent.getBottom(), p);
        }
        //c.drawRect(0, 0, right, bottom, p);
        // 画个背景，然后view挡上去，露出的的地方就是decoration
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(gap,gap,gap,gap);//上下左右分别撑开
    }

}
