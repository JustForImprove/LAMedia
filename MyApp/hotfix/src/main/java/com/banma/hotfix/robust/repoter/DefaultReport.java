package com.banma.hotfix.robust.repoter;

import android.util.Log;

// 报告实现类，可用于先服务器提交数据
public class DefaultReport implements Reporter {
    @Override
    public void report(String tag, Object obj) {
        Log.d("Reporter", "tag: " + tag + ", obj: " + obj);
    }
}
