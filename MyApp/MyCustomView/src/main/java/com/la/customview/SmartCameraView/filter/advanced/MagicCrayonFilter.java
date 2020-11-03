package com.la.customview.SmartCameraView.filter.advanced;

import android.opengl.GLES20;

import com.la.customview.R;
import com.la.customview.SmartCameraView.filter.base.gpuimage.GPUImageFilter;
import com.la.customview.SmartCameraView.filter.utils.MagicFilterType;

public class MagicCrayonFilter extends GPUImageFilter {
    
    private int mSingleStepOffsetLocation;
    //1.0 - 5.0
    private int mStrengthLocation;
    
    public MagicCrayonFilter(){
        super(MagicFilterType.CRAYON, R.raw.crayon);
    }

    @Override
    protected void onInit() {
        super.onInit();
        mSingleStepOffsetLocation = GLES20.glGetUniformLocation(getProgram(), "singleStepOffset");
        mStrengthLocation = GLES20.glGetUniformLocation(getProgram(), "strength");
        setFloat(mStrengthLocation, 2.0f);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onInitialized(){
        super.onInitialized();
        setFloat(mStrengthLocation, 0.5f);
    }

    @Override
    public void onInputSizeChanged(final int width, final int height) {
        setFloatVec2(mSingleStepOffsetLocation, new float[] {1.0f / width, 1.0f / height});
    }
}
