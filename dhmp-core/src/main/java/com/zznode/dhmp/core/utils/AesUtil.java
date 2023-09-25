package com.zznode.dhmp.core.utils;


import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * <p>Title: AESUtil</p>
 * <p>Description: </p>
 *
 * @author L丶慕留人
 * @author 王俊
 * @version 1.0.0
 * @date 2020/5/27 17:32
 */
public class AesUtil {

    private static final String PRIVATE_KEY = "cdqj@p0ssw0rd#*!";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_STR = "AES/ECB/ISO10126Padding";

    /**
     * SecretKeySpec类是KeySpec接口的实现类,用于构建秘密密钥规范
     */
    private static final SecretKeySpec SECRET_KEY_SPEC = new SecretKeySpec(PRIVATE_KEY.getBytes(), ALGORITHM);

    /**
     * AES加密
     */
    public static String encryptData(String data) {
        try {
            // 创建密码器
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
            // 初始化
            cipher.init(Cipher.ENCRYPT_MODE, SECRET_KEY_SPEC);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes()));
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException("error occurred while encrypt. ", e);
        }
    }

    /**
     * AES解密
     */
    public static String decryptData(String base64Data) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_STR);
            cipher.init(Cipher.DECRYPT_MODE, SECRET_KEY_SPEC);
            return new String(cipher.doFinal(Base64.getDecoder().decode(base64Data)));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException("error occurred while decrypt. ", e);
        }
    }

}