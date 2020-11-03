package com.banma.hotfix.robust;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.banma.hotfix.robust.downloader.PatchDownloader;
import com.banma.hotfix.robust.processor.PatchProcessor;
import com.banma.hotfix.robust.repoter.Reporter;
import com.banma.hotfix.util.AppLog;
import com.banma.hotfix.util.MD5Tools;
import com.meituan.robust.Patch;
import com.meituan.robust.PatchManipulate;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// 补丁管理器实现类
public class PatchManipulateImpl extends PatchManipulate {

    // 补丁文件夹
    private static final String PATCH_DIR = "patches";
    // 补丁名
    private static final String PATCH_NAME = "patch";

    /* 忽略AppHash验证 */
    private boolean ignoreAppHash = false;

    // 补丁下载器
    private PatchDownloader downloader;
    // 补丁解密处理
    private PatchProcessor processor;
    // 报告
    private Reporter reporter;
    // 补丁缓存集合
    private Set<String> mPatchedCache;

    // 补丁管理器构造方法
    public PatchManipulateImpl(PatchDownloader downloader, PatchProcessor processor, Reporter reporter) {
        this.downloader = downloader;
        this.processor = processor;
        this.reporter = reporter;
        this.mPatchedCache = new TreeSet<>();
    }

    // 补丁md5缓存添加方法
    public void addCache(String md5) {
        if (mPatchedCache == null) {
            mPatchedCache = new TreeSet<>();
        }
        AppLog.step(13, "save patched md5: " + md5);
        mPatchedCache.add(md5);
    }
    // 设置是否忽略appHash
    public void setIgnoreAppHash(boolean ignoreAppHash) {
        this.ignoreAppHash = ignoreAppHash;
    }

    // 补丁管理实现构造方法
    public PatchManipulateImpl(PatchDownloader downloader, PatchProcessor processor) {
        this(downloader, processor, null);
    }

    // 获取补丁列表
    @Override
    protected List<Patch> fetchPatchList(Context context) {
        AppLog.d("fetchPatchList: ");
        return downloader.fetchPatchListSync(getLocalDir(context) + File.separator + PATCH_DIR);
    }

    // 验证补丁
    @Override
    protected boolean verifyPatch(Context context, Patch patch) {
        AppLog.d("verifyPatch: ");
        //不进行校验，直接下载
        File targetFile = new File(patch.getLocalPath());
        Log.e("文件路径",patch.getLocalPath());
        if (!targetFile.exists()){
            Log.e("ddddddddddd","文件不存在");
            return false;
        }
        // roubust 最终会从temp_path获取jar, 我吐了，
        Log.e("PatchMan","本地不进行校验");

        patch.setTempPath("/storage/emulated/0/Android/data/com.banma.rooclassai/files/patches/patch");

        File f = new File(patch.getTempPath());
        if (f.exists()){
            Log.e("dddddddddd","fffffff");
        }else {
            Log.e("dddddddddd",patch.getTempPath() +" 文件不存在");
        }

        return true;
        /*
        AppLog.step(6, "assert local file md5");
        if (!assertFileMD5(patch.getLocalPath(), patch.getMd5())) {
            AppLog.step(6, "local file md5 not matched. "+ patch.getMd5() + "<->" + getFileMd5(patch.getLocalPath()));
            doReport("failed_with_local_md5_not_match_" + patch.getMd5() + "<->" + getFileMd5(patch.getLocalPath()));
            return false;
        }
        AppLog.step(6, "local file md5 matched.");
        AppLog.step(7, "check apk hash");
        String robustApkHash = RobustApkHashUtils.readRobustApkHash(context);
        AppLog.d("verifyPatch: robustApkHash: " + robustApkHash);
        if (!ignoreAppHash && !TextUtils.equals(patch.getAppHash(), robustApkHash)) {
            AppLog.step(7, "app hash not matched. " + patch.getAppHash() + "<->" + robustApkHash);
            doReport("failed_with_app_hash_not_match_" + patch.getAppHash() + "<->" + robustApkHash);
            return false;
        }
        AppLog.step(7, "app hash matched or ignored: " + ignoreAppHash);
        File tempFile = new File(context.getCacheDir() + File.separator + PATCH_NAME);
        AppLog.step(8, "check temp file lock status");
        if (isFileLocked(tempFile)) {
            AppLog.step(8, "temp file has been locked.");
            // 文件不可删除，可能被恶意占用
            doReport("tempFile be locked.");
            return false;
        }
        AppLog.step(8, "temp file is normally.");
        patch.setTempPath(context.getCacheDir() + File.separator + PATCH_NAME);
        if (patch instanceof CryptPatch) {
            try {
                AppLog.step(9, "decrypt encoded key by rsa");
                String rsaContent = ((CryptPatch) patch).getRsaKey();
                AppLog.d("verifyPatch: rsa content: " + rsaContent);
                processor.decodePatch(context, rsaContent, patch);
                String tempMD5 = ((CryptPatch) patch).getTempMD5();
                AppLog.step(11, "assert temp file md5");
                if (!assertFileMD5(patch.getTempPath(), tempMD5)) {
                    AppLog.step(11, "temp file md5 not matched.");
                    // 如果md5不正确，报告不正确，并回传MD5值
                    doReport("failed_with_temp_md5_not_match_" + tempMD5 + "<->" + getFileMd5(patch.getTempPath()));
                    return false;
                }
                AppLog.step(11, "temp file md5 matched.");
            } catch (Exception e) {
                doReport("failed_with_decode_" + e.getMessage());
                AppLog.e("verifyPatch: decodePatch exception: ", e);
                return false;
            }
        }
        return true;
        */
    }

    // 确认补丁是否存在，未存在的需要下载
    @Override
    protected boolean ensurePatchExist(Patch patch) {
        Log.e("PatchMan","ensure");
        return downloader.downloadSync(patch.getUrl(), patch.getLocalPath());
        /*
        AppLog.d("ensurePatchExist: ");
        AppLog.step(2, "check is already patched: " + patch.getMd5());
        if (mPatchedCache != null && mPatchedCache.contains(patch.getMd5())) {
            // 已经加载过该补丁
            AppLog.step(2, "is already patched");
            AppLog.d("ensurePatchExist: already patched.");
            return false;
        }
        AppLog.step(2, "not patched yet.");
        AppLog.step(3, "is patch file downloaded");
        if (assertFileMD5(patch.getLocalPath(), patch.getMd5())) {
            AppLog.step(3, "patch file is downloaded.");
            return true;
        }
        AppLog.step(3, "patch file not download yet.");
        File localFile = new File(patch.getLocalPath());
        AppLog.step(4, "check is local file locked");
        if (isFileLocked(localFile)) {
            // 文件不可删除，可能被恶意占用
            AppLog.step(4, "local file has been locked");
            doReport("localFile be locked.");
            return false;
        }
        AppLog.step(4, "local file is normally.");
        return downloader.downloadSync(patch.getUrl(), patch.getLocalPath());
        // && assertFileMD5(patch.getLocalPath(), patch.getMd5());

         */
    }

    // 删除文件并判断是否被删除，当文件被打开时是无法删除的
    private boolean isFileLocked(File file) {
        boolean deletable = true;
        if (file.exists()) {
            deletable = file.delete();
        }

        return !deletable;
    }

    /**
     *  验证文件md5
     * @param path
     * @param except
     * @return
     */
    // 验证补丁文件是否存在以及MD5正确性
    private boolean assertFileMD5(String path, String except) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(except)) return false;
        File targetFile = new File(path);
        if (!targetFile.exists()) return false;
        try {
            String md5 = MD5Tools.getFileMD5(targetFile);
            AppLog.d("assertFileMD5: except md5: " + except);
            AppLog.d("assertFileMD5: file md5: " + md5);
            return TextUtils.equals(md5, except);
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    // 获取本地文件夹路径
    private String getLocalDir(Context context) {
        String packageHome = "/Android/data/" + context.getPackageName() + "/files";
        return new File(Environment.getExternalStorageDirectory(), packageHome).getAbsolutePath();
    }

    //获取文件的md5值
    private String getFileMd5(String path) {
        String md5 = "-1";
        if (TextUtils.isEmpty(path)) return md5;
        File targetFile = new File(path);
        if (!targetFile.exists()) return md5;
        try {
            md5 = MD5Tools.getFileMD5(targetFile);
        } catch (FileNotFoundException e) {
        }
        return md5;
    }


    private static final String TAG_PATCH_STATUS = "robust_patch_status";
    // 报告方法
    private void doReport(String status) {
        if (reporter == null) return;
        reporter.report(TAG_PATCH_STATUS, status);
    }
}
