package com.banma.hotfix.robust.processor;

import android.content.Context;

import com.meituan.robust.Patch;

public interface PatchProcessor {
    /**
     * 解密原始patch文件
     * @param patch
     */
    void decodePatch(Context context, String cryptoGraph, Patch patch) throws ProcessException;
}
