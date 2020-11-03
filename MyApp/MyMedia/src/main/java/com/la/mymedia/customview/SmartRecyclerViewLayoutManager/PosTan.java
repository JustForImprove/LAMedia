package com.la.mymedia.customview.SmartRecyclerViewLayoutManager;
// 一个数据封装类
public class PosTan {
    /**
     * 在路径上的位置 (百分比)
     */
    public float fraction;

    /**
     * Item所对应的索引
     */
    public int index;
    public float x;
    public float y;

    /**
     * Item的旋转角度
     */
    private float angle;

    public PosTan(float fraction) {
        this.fraction = fraction;
    }



    public float getChildAngle() {
        return angle;
    }


    public void set(float x, float y, float angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public void setIndex(int index){
        this.index = index;
    }
}
