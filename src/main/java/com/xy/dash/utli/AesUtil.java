package com.xy.dash.utli;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
public class AesUtil {

    public static final String AES_KEY = "124hrfskdjf2349ru3rfkjre34hfijkr";


    /**
     * 加密
     *
     * @param input
     * @return
     */
    public static String encrypt(String input) {
        byte[] bytes = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey keySpec = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            bytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw unchecked(e);
        }

        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
    }

    /**
     * 解密
     *
     * @param input
     * @return
     */
    public static String decrypt(String input, boolean isLog) {
        byte[] bytes = null;
        try {
            byte[] decode = Base64.getDecoder().decode(input);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            SecretKey keySpec = new SecretKeySpec(AES_KEY.getBytes(StandardCharsets.UTF_8), "AES");
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            bytes = cipher.doFinal(decode);
        } catch (Exception e) {
            if (isLog) {
                log.error("解密失败,还原明文", e);
            }
            return input;
        }
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String decrypt(String input) {
        return decrypt(input, true);
    }

    public static RuntimeException unchecked(Throwable e) {
        if (e instanceof IllegalAccessException || e instanceof IllegalArgumentException
                || e instanceof NoSuchMethodException) {
            return new IllegalArgumentException(e);
        } else if (e instanceof InvocationTargetException) {
            return new RuntimeException(((InvocationTargetException) e).getTargetException());
        } else if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        } else {
            return new RuntimeException(e);
        }
    }

}
