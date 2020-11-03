package com.banma.hotfix.robust.downloader;

import com.meituan.robust.Patch;

import java.util.List;

public interface PatchDownloader {

    boolean downloadSync(String url, String path);

    List<Patch> fetchPatchListSync(String dir);
}
