package com.banma.hotfix.robust.model;

import com.meituan.robust.Patch;

public class CryptPatch extends Patch {
    /* rsa加密后的密钥 */
    private String rsaKey;
    private String tempMD5;

    public String getTempMD5() {
        return tempMD5;
    }

    public void setTempMD5(String tempMD5) {
        this.tempMD5 = tempMD5;
    }

    public String getRsaKey() {
        return rsaKey;
    }

    public void setRsaKey(String rsaKey) {
        this.rsaKey = rsaKey;
    }
}
