package com.banma.hotfix.robust.processor;

import android.content.Context;

import com.banma.hotfix.robust.crypto.FileAESUtil;
import com.banma.hotfix.robust.crypto.RSAUtil;
import com.banma.hotfix.util.AppLog;
import com.meituan.robust.Patch;

import java.io.InputStream;

public class DefaultPatchProcessor implements PatchProcessor {

    @Override
    public void decodePatch(Context context, String rsaKey, Patch patch) throws ProcessException {
        try {
            AppLog.step(10, "decrypt...");
            InputStream inputStream = context.getAssets().open("rsa.pub");
            String graph = RSAUtil.decrypt(rsaKey, inputStream);
            AppLog.d("decodePatch: decrypt graph: " + graph);
            FileAESUtil.decrypt(patch.getLocalPath(), patch.getTempPath(), graph);
            AppLog.step(10, "decrypt success.");
        } catch (Exception e) {
            AppLog.step(10, "decrypt failed.");
            throw new ProcessException(e.getMessage());
        }
    }
}
