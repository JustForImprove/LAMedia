package com.banma.hotfix.robust;

import android.content.Context;
import android.util.Log;


import com.banma.hotfix.robust.downloader.DefaultPatchDownloader;
import com.banma.hotfix.robust.downloader.PatchDownloader;
import com.banma.hotfix.robust.processor.DefaultPatchProcessor;
import com.banma.hotfix.robust.processor.PatchProcessor;
import com.banma.hotfix.robust.repoter.DefaultReport;
import com.banma.hotfix.robust.repoter.Reporter;
import com.banma.hotfix.util.AppLog;
import com.meituan.robust.Patch;
import com.meituan.robust.PatchExecutor;
import com.meituan.robust.RobustCallBack;

import java.util.List;

public class HotPatchManager {
    //./gradlew clean  assembleRelease --stacktrace --no-daemon
    // adb push patch.jar /sdcard/Android/data/com.bbae.anno/files/loader
    private static HotPatchManager ins;

    private static final String TAG_PATCH_STATUS = "robust_patch_status";

    private PatchExecutor executor;
    private PatchDownloader downloader;
    private PatchProcessor processor;
    private Reporter reporter;
    private Context context;
    //
    private PatchManipulateImpl mPatchManipulate;

    public static HotPatchManager getIns(Context context) {
        if (ins == null) {
            synchronized (HotPatchManager.class) {
                if (ins == null) {
                    ins = new HotPatchManager(context);
                }
            }
        }
        return ins;
    }

    private HotPatchManager(Context context) {
        this.context = context;
        this.downloader = new DefaultPatchDownloader(context);
        this.processor = new DefaultPatchProcessor();
        this.reporter = new DefaultReport();
    }

    public HotPatchManager setDownloader(PatchDownloader downloader) {
        this.downloader = downloader;
        return this;
    }

    public HotPatchManager setProcessor(PatchProcessor processor) {
        this.processor = processor;
        return this;
    }

    // 热更新管理器初始化方法,不实现监听逻辑
    public HotPatchManager init() {

        return init(context, new RobustCallBack() {
            @Override
            public void onPatchListFetched(boolean result, boolean isNet, List<Patch> patches) {
                AppLog.d("onPatchListFetched: result: " + result);
            }

            @Override
            public void onPatchFetched(boolean result, boolean isNet, Patch patch) {
                AppLog.d("onPatchFetched: result: " + result);
            }

            @Override
            public void onPatchApplied(boolean result, Patch patch) {
                Log.e("PatchApplied", "");
                AppLog.step(12, "patch result: " + (result?"success.": "failed"));
                if (result) {
                    doReport("success");
                    if (mPatchManipulate != null) {
                        mPatchManipulate.addCache(patch.getMd5());
                    }
                } else {
                    doReport("failed_in_patch_" + patch.getName());
                }
                AppLog.d("onPatchApplied: result: " + result +
                        ", isAppliedSuccess: " + patch.isAppliedSuccess() +
                        ", md5: " + patch.getMd5()
                );
                AppLog.step(0, "Robust Finished!");
            }

            @Override
            public void logNotify(String log, String where) {
                doReport("failed_log: " + log + "|where: " + where);
                AppLog.d("logNotify: log: " + log + " - where: " + where);
            }

            @Override
            public void exceptionNotify(Throwable throwable, String where) {
                AppLog.d("exceptionNotify: where: " + where + " - exception: " + throwable.getMessage());
                doReport("patch_failed_with_exception_" + throwable.getMessage());
            }
        });
    }

    // 报告方法
    private void doReport(String status) {
        if (null == reporter) return;
        reporter.report(TAG_PATCH_STATUS, status);
    }
    // app上下文
    private Context mContext;
    // robust回调
    private RobustCallBack mCallback;
    // 热加载管理器初始化, 实现了监听逻辑
    public HotPatchManager init(Context context, RobustCallBack callback) {
        AppLog.d("init: ");
        if (downloader == null) {
            throw new IllegalArgumentException("PatchDownloader cannot be null");
        }
        if (processor == null) {
            throw new IllegalArgumentException("PatchProcessor cannot be null");
        }
        this.mContext = context;
        this.mCallback = callback;
        // 补丁控制类实例化
        mPatchManipulate = new PatchManipulateImpl(downloader, processor, reporter);

        return this;
    }
    // 执行补丁
    public void execute() {
        AppLog.step(0, "Robust Start!");
        // 如果补丁执行线程在运行，则直接中断
        if (executor != null) {
            executor.interrupt();
        }
        // 新建一个补丁执行器
        // 传入app上下文，补丁控制器，补丁执行过程回调方法
        executor = new PatchExecutor(
                mContext,
                mPatchManipulate,
                mCallback
        );
        // 开始执行补丁
        executor.start();
    }

}
