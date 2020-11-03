package com.banma.hotfix.robust.downloader;

import android.content.Context;
import android.util.Log;

import com.banma.hotfix.robust.model.CryptPatch;
import com.banma.hotfix.util.AppLog;
import com.banma.hotfix.util.IoUtils;
import com.meituan.robust.Patch;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class DefaultPatchDownloader implements PatchDownloader {

    private OkHttpClient client;
    private Context context;
    public DefaultPatchDownloader(Context context) {
        this.client = new OkHttpClient.Builder().build();
        this.context = context;
    }

    // 下载补丁实体
    @Override
    public boolean downloadSync(String url, String path) {

        Log.e("patchDownloader","下载开始"+path);
        // 从本地assets读取放入path逻辑
        InputStream inputStream = null;
        OutputStream outputStream = null;


        try {
            final File destFile = newAndCreateFile(path);
            // 打开assets获得文件输入流
            inputStream = context.getAssets().open(url);
            // 将输出流 输出到目标文件
            outputStream = new FileOutputStream(destFile);
            // 拷贝流
            IoUtils.copyStream(inputStream, outputStream, new IoUtils.CopyListener() {
                @Override
                public boolean onBytesCopied(int current, int total) {
                    AppLog.d("onBytesCopied: download: " + current + "/" + total);
                    Log.e("文件是否拷贝成功","拷贝成功");
                    Log.e("文件是否存在", String.valueOf(destFile.exists()));
                    return true;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 从服务器下载逻辑
        /*
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(path)) return false;
        AppLog.step(5, "do download patch file sync.");
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
        InputStream inputStream = null;
        OutputStream outputStream = null;
        Response response = null;
        try {
            response = client.newCall(request).execute();
            inputStream = response.body().byteStream();
            if (inputStream == null) return false;
            // 获取本地文件对象
            File destFile = newAndCreateFile(path);
            // 打开本地文件输出流, 从程序内存输出的叫输出流，输入程序内存的叫输入流
            outputStream = new FileOutputStream(destFile);
            // 拷贝流
            IoUtils.copyStream(inputStream, outputStream, new IoUtils.CopyListener() {
                @Override
                public boolean onBytesCopied(int current, int total) {
                    AppLog.d("onBytesCopied: download: " + current + "/" + total);
                    return true;
                }
            });
            AppLog.step(5, "download success.");
        } catch (IOException e) {
            AppLog.step(5, "download patch exception. " + e.getMessage());
            return false;
        } catch (Exception e) {
            AppLog.step(5, "download patch exception. " + e.getMessage());
        } finally {
            IoUtils.closeSilently(inputStream);
            IoUtils.closeSilently(outputStream);
            if (response != null) response.close();
        }

         */
        return true;
    }

    private File newAndCreateFile(String path) throws IOException {
        File file = new File(path);
        File dir = file.getParentFile();
        if (!dir.exists()) dir.mkdirs();
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        return file;
    }

    // 和robust.xml中patchPackname必须保持一致
    private static final String PKG_NAME = "com.banma.rooclassai.patch";
    private static final String DOT = ".";
    private static final String CLZ_NAME = "PatchesInfoImpl";
    // 获取补丁列表， 有哪些补丁,以及补丁信息
    @Override
    public List<Patch> fetchPatchListSync(String dir) {
        AppLog.step(1, "fetch patch list.");
        // 从本地加载逻辑
        List<Patch> patches = new ArrayList<>(5);
        CryptPatch patch = new CryptPatch();
        patch.setName("patch");
        patch.setMd5("");
        patch.setAppHash("");
        // 设置本地存储路径
        patch.setLocalPath(dir + File.separator + "patch");
        patch.setTempMD5("");
        // assets中补丁文件名
        patch.setUrl("patch.jar");
        patch.setRsaKey("");
        // 设置补丁信息类全名，用于描述有多少补丁，以及补丁的状况
        patch.setPatchesInfoImplClassFullName(PKG_NAME + DOT + CLZ_NAME);
        patches.add(patch);
        return patches;
        // 从服务器加载逻辑
        /*
        Request request = new Request.Builder()
                .get()
                .url("http://yapi.demo.qunar.com/mock/27462/ceshi/patches?versionCode=0")
                .build();
        Call call = client.newCall(request);
        List<Patch> patches = new ArrayList<>(5);
        Response response = null;
        try {
            response = call.execute();
            String json = response.body().string();
            //json = "{'d',}";
            JSONArray ja = new JSONArray(json);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = ja.getJSONObject(i);
                CryptPatch patch = new CryptPatch();
                patch.setName(jo.getString("name"));
                patch.setMd5(jo.getString("md5"));
                patch.setAppHash(jo.getString("appHash"));
                patch.setLocalPath(dir + File.separator + "patch");
                patch.setTempMD5(jo.getString("tmpMD5"));
                patch.setUrl(jo.getString("path"));
                patch.setRsaKey(jo.getString("password"));
                patch.setPatchesInfoImplClassFullName(PKG_NAME + DOT + CLZ_NAME);
                patches.add(patch);
            }
            AppLog.step(1, "patch list size: " + patches.size());
            response.close();
        } catch (IOException e) {
            AppLog.step(1, "fetch patch list io exception. " + e.getMessage());
            e.printStackTrace();
        } catch (JSONException e) {
            AppLog.step(1, "fetch patch list  json exception. " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (response != null) response.close();
        }

        return patches;
  */
    }
}
