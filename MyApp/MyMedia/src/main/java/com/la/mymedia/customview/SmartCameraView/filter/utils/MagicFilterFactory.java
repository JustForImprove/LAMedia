package com.la.mymedia.customview.SmartCameraView.filter.utils;


import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicAmaroFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicAntiqueFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicBeautyFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicBlackCatFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicBrannanFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicBrooklynFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicCalmFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicCoolFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicCrayonFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicEarlyBirdFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicEmeraldFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicEvergreenFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicFreudFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicHealthyFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicHefeFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicHudsonFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicImageAdjustFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicInkwellFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicKevinFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicLatteFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicLomoFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicN1977Filter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicNashvilleFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicNostalgiaFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicPixelFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicRiseFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicRomanceFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicSakuraFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicSierraFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicSketchFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicSkinWhitenFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicSunriseFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicSunsetFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicSutroFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicSweetsFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicTenderFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicToasterFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicValenciaFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicWaldenFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicWarmFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicWhiteCatFilter;
import com.la.mymedia.customview.SmartCameraView.filter.advanced.MagicXproIIFilter;
import com.la.mymedia.customview.SmartCameraView.filter.base.MagicLookupFilter;

import com.la.mymedia.customview.SmartCameraView.filter.base.gpuimage.GPUImageBrightnessFilter;
import com.la.mymedia.customview.SmartCameraView.filter.base.gpuimage.GPUImageContrastFilter;
import com.la.mymedia.customview.SmartCameraView.filter.base.gpuimage.GPUImageExposureFilter;
import com.la.mymedia.customview.SmartCameraView.filter.base.gpuimage.GPUImageFilter;
import com.la.mymedia.customview.SmartCameraView.filter.base.gpuimage.GPUImageHueFilter;
import com.la.mymedia.customview.SmartCameraView.filter.base.gpuimage.GPUImageSaturationFilter;
import com.la.mymedia.customview.SmartCameraView.filter.base.gpuimage.GPUImageSharpenFilter;

public class MagicFilterFactory{

    public static GPUImageFilter initFilters(MagicFilterType type) {
        switch (type) {
            case NONE:
                return new GPUImageFilter();
            case WHITECAT:
                return new MagicWhiteCatFilter();
            case BLACKCAT:
                return new MagicBlackCatFilter();
            case SKINWHITEN:
                return new MagicSkinWhitenFilter();
            case BEAUTY:
                return new MagicBeautyFilter();
            case ROMANCE:
                return new MagicRomanceFilter();
            case SAKURA:
                return new MagicSakuraFilter();
            case AMARO:
                return new MagicAmaroFilter();
            case WALDEN:
                return new MagicWaldenFilter();
            case ANTIQUE:
                return new MagicAntiqueFilter();
            case CALM:
                return new MagicCalmFilter();
            case BRANNAN:
                return new MagicBrannanFilter();
            case BROOKLYN:
                return new MagicBrooklynFilter();
            case EARLYBIRD:
                return new MagicEarlyBirdFilter();
            case FREUD:
                return new MagicFreudFilter();
            case HEFE:
                return new MagicHefeFilter();
            case HUDSON:
                return new MagicHudsonFilter();
            case INKWELL:
                return new MagicInkwellFilter();
            case KEVIN:
                return new MagicKevinFilter();
            case LOCKUP:
                return new MagicLookupFilter("");
            case LOMO:
                return new MagicLomoFilter();
            case N1977:
                return new MagicN1977Filter();
            case NASHVILLE:
                return new MagicNashvilleFilter();
            case PIXAR:
                return new MagicPixelFilter();
            case RISE:
                return new MagicRiseFilter();
            case SIERRA:
                return new MagicSierraFilter();
            case SUTRO:
                return new MagicSutroFilter();
            case TOASTER2:
                return new MagicToasterFilter();
            case VALENCIA:
                return new MagicValenciaFilter();
            case XPROII:
                return new MagicXproIIFilter();
            case EVERGREEN:
                return new MagicEvergreenFilter();
            case HEALTHY:
                return new MagicHealthyFilter();
            case COOL:
                return new MagicCoolFilter();
            case EMERALD:
                return new MagicEmeraldFilter();
            case LATTE:
                return new MagicLatteFilter();
            case WARM:
                return new MagicWarmFilter();
            case TENDER:
                return new MagicTenderFilter();
            case SWEETS:
                return new MagicSweetsFilter();
            case NOSTALGIA:
                return new MagicNostalgiaFilter();
            case SUNRISE:
                return new MagicSunriseFilter();
            case SUNSET:
                return new MagicSunsetFilter();
            case CRAYON:
                return new MagicCrayonFilter();
            case SKETCH:
                return new MagicSketchFilter();
            //image adjust
            case BRIGHTNESS:
                return new GPUImageBrightnessFilter();
            case CONTRAST:
                return new GPUImageContrastFilter();
            case EXPOSURE:
                return new GPUImageExposureFilter();
            case HUE:
                return new GPUImageHueFilter();
            case SATURATION:
                return new GPUImageSaturationFilter();
            case SHARPEN:
                return new GPUImageSharpenFilter();
            case IMAGE_ADJUST:
                return new MagicImageAdjustFilter();
            default:
                return null;
        }
    }
}
