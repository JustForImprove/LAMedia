package com.banma.hotfix.robust.crypto;

import android.util.Base64;

import com.banma.hotfix.util.AppLog;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.security.Key;

import javax.crypto.Cipher;

public class RSAUtil {
    /** 指定加密算法为RSA */
    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";

    /**
     * 加密方法
     * @param source 源数据
     * @return
     * @throws Exception
     */
    public static String encrypt(String source, InputStream keyStream) throws Exception {

        Key privateKey = getKey(keyStream);
        AppLog.d("encrypt: 私钥为：");
        AppLog.d(Base64.encodeToString(privateKey.getEncoded(), Base64.DEFAULT));

        /* 得到Cipher对象来实现对源数据的RSA加密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] b = source.getBytes();
        /* 执行加密操作 */
        byte[] b1 = cipher.doFinal(b);
        return Base64.encodeToString(b1, Base64.DEFAULT);
    }

    /**
     * 解密算法
     * @param rsaContent    密文
     * @return
     * @throws Exception
     */
    public static String decrypt(String rsaContent, InputStream keyStream) throws Exception {

        Key publicKey = getKey(keyStream);

        AppLog.d("decrypt: 公钥为：");
        AppLog.d(Base64.encodeToString(publicKey.getEncoded(), Base64.DEFAULT));

        /* 得到Cipher对象对已用公钥加密的数据进行RSA解密 */
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] rsaContentBytes = Base64.decode(rsaContent, Base64.DEFAULT);

        /* 执行解密操作 */
        byte[] contentBytes = cipher.doFinal(rsaContentBytes);
        return new String(contentBytes);
    }

    private static Key getKey(InputStream is) throws Exception {
        Key key;
        ObjectInputStream ois = null;
        try {
            /* 将文件中的私钥对象读出 */
            ois = new ObjectInputStream(is);
            key = (Key) ois.readObject();
        } catch (Exception e) {
            throw e;
        } finally {
            ois.close();
        }
        return key;
    }
}
