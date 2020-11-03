package com.la.mymedia.customview.SmartRecyclerViewLayoutManager;

import android.graphics.Path;
import android.graphics.PathMeasure;

import androidx.annotation.FloatRange;

public class Keyframes {
    private float PRECISION = 1;
    private float[] mX; //Path的所有x轴坐标点
    private float[] mY; //Path的所有y轴坐标点
    private float[] mAngle; //Path上每一个坐标所对应的角度
    int mNumPoints;
    // 所有item的坐标
    PosTan[] posTans;
    float pathLength;

    public Keyframes(Path path){
        initPath(path);
    }
    private void initPath(Path path) {
        PathMeasure pathMeasure = new PathMeasure(path, false);
        pathLength = pathMeasure.getLength();
        mNumPoints = (int) (pathLength / PRECISION) + 1;
        // 初始化坐标点内存
        mX = new float[mNumPoints];
        mY = new float[mNumPoints];
        mAngle = new float[mNumPoints];
         //临时存放坐标点
        float[] position = new float[2];
        //临时存放正切值
        float[] tangent = new float[2];
        //当前距离
        float distance;
        for (int i = 0; i < mNumPoints; ++i) {
            //更新当前距离
            distance = (i * pathLength) / (mNumPoints - 1);
            //根据当前距离获取对应的坐标点和正切值
            pathMeasure.getPosTan(distance, position, tangent);
            mX[i] = position[0];
            mY[i] = position[1];
            //利用反正切函数得到角度
            mAngle[i] = fixAngle((float) (Math.atan2(tangent[1], tangent[0]) * 180F / Math.PI));
        }
    }


    /**
     * 调整角度，使其在0 ~ 360之间
     *
     * @param rotation 当前角度
     * @return 调整后的角度
     */
    private float fixAngle(float rotation) {
        float angle = 360F;
        if (rotation < 0) {
            rotation += angle;
        }
        if (rotation > angle) {
            rotation %= angle;
        }
        return rotation;
    }


    public int getPathLength() {
        return (int) pathLength;
    }

    public PosTan getValue(@FloatRange(from = 0F, to = 1F) float fraction) {
        //超出范围的直接返回空
        PosTan posTan = new PosTan(fraction);
        if (fraction >= 1F || fraction < 0) {
            return null;
        } else {
            int index = (int) (mNumPoints * fraction);
            //更新temp的内部值
            posTan.set(mX[index], mY[index], mAngle[index]);
            return posTan;
        }
    }

}

