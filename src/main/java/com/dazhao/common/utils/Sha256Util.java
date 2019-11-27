package com.dazhao.common.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;

@Slf4j
public class Sha256Util {

    private Sha256Util() {
    }

    /**
     * 利用Apache的工具类实现SHA-256加密
     *
     * @param str 加密后的报文
     */
    public static String string2SHA256(String str) {
        MessageDigest messageDigest;
        String encdeStr = "";
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            byte[] hash = messageDigest.digest(str.getBytes(StandardCharsets.UTF_8));
            encdeStr = Hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            log.error("字符串加密报错:", e);
        }
        return encdeStr;
    }

    /**
     * 小文件计算方法
     */
    public static String signatureImageHash(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(data);
            byte[] byteHash = md.digest();
            String signatureHash = Hex.encodeHexString(byteHash);
            return signatureHash;
        } catch (Exception e) {
            log.info("签署请求时无法计算哈希:" + e.getMessage(), e);
            return null;
        }

    }
}
