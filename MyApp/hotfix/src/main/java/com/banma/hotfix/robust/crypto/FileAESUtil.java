package com.banma.hotfix.robust.crypto;

import android.text.TextUtils;


import com.banma.hotfix.util.AppLog;
import com.banma.hotfix.util.IoUtils;
import com.banma.hotfix.util.MD5Tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileAESUtil {

    private static final String ALGORITHM = "AES";
    private static final int KEY_SIZE = 128;

    public static void encrypt(File src, File encryptDest, String key) {
        key = MD5Tools.toMD5(key);

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        CipherOutputStream cipherOutputStream = null;

        try {
            if (encryptDest.exists()) {
                encryptDest.delete();
            }

            Cipher cipher = getAesCipher(key, Cipher.ENCRYPT_MODE);

            inputStream = new FileInputStream(src);
            outputStream = new FileOutputStream(encryptDest);
            cipherOutputStream = new CipherOutputStream(outputStream, cipher);

            IoUtils.copyStream(inputStream, cipherOutputStream, new IoUtils.CopyListener() {
                @Override
                public boolean onBytesCopied(int current, int total) {
                    AppLog.d("onBytesCopied: encrypted: " + current + "/" + total);
                    return true;
                }
            });

        } catch (Exception e) {
            AppLog.d("decrypt: exception: " + e.getMessage());
        } finally {
            IoUtils.closeSilently(cipherOutputStream);
            IoUtils.closeSilently(inputStream);
            IoUtils.closeSilently(outputStream);
        }
    }

    public static void decrypt(File encryptSrc, File decryptDest, String key) {
        key = MD5Tools.toMD5(key);

        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        CipherOutputStream cipherOutputStream = null;
        try {
            if (decryptDest.exists()) {
                decryptDest.delete();
            }

            Cipher cipher = getAesCipher(key, Cipher.DECRYPT_MODE);

            inputStream = new FileInputStream(encryptSrc);
            outputStream = new FileOutputStream(decryptDest);
            cipherOutputStream = new CipherOutputStream(outputStream, cipher);

            IoUtils.copyStream(inputStream, cipherOutputStream, new IoUtils.CopyListener() {
                @Override
                public boolean onBytesCopied(int current, int total) {
                    AppLog.d("onBytesCopied: decrypted: " + current + "/" + total);
                    return true;
                }
            });

        } catch (Exception e) {
            AppLog.d("decrypt: exception: " + e.getMessage());
        } finally {
            IoUtils.closeSilently(cipherOutputStream);
            IoUtils.closeSilently(inputStream);
            IoUtils.closeSilently(outputStream);
        }

    }

    public static void encrypt(String srcPath, String encryptDestPath, String key) {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(encryptDestPath)) return;
        encrypt(new File(srcPath), new File(encryptDestPath), key);
    }

    public static void decrypt(String encryptSrcPath, String decryptDestPath, String key) {
        if (TextUtils.isEmpty(encryptSrcPath) || TextUtils.isEmpty(decryptDestPath)) return;
        decrypt(new File(encryptSrcPath), new File(decryptDestPath), key);
    }

    public static final String VIPARA = "0102030405060708";

    private static Cipher getAesCipher(String key, int mode)
            throws NoSuchPaddingException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, InvalidKeyException {
        IvParameterSpec zeroIv = new IvParameterSpec(VIPARA.getBytes());
        SecretKeySpec sKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(mode, sKey, zeroIv);
        return cipher;
    }

}
