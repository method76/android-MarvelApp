package com.method76.common.cipher;

import com.method76.common.util.ByteUtil;

import org.apache.commons.codec.binary.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Sungjoon Kim on 2016-02-05.
 */
public class CipherUtil {

    /**
     * SHA-512 암호화 Using seed
     * @param orgMsg
     * @param salt
     * @return
     */
    public static String getSHA512Hash(String orgMsg, String salt)
    {
        String plainText = orgMsg + salt;
        MessageDigest messageDigest;
        byte[] hash = null;

        try {
            messageDigest = MessageDigest.getInstance("SHA-512");
            hash = messageDigest.digest( plainText.getBytes() );
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return ByteUtil.toHexString(hash).toLowerCase();
    }


    /**
     * AES256 : ECB/PKCS5Padding 암호화
     * @param msg
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String AES256Encode(String msg, String secretKey) throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException{

        byte[] keyData = secretKey.getBytes();
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        c.init(Cipher.ENCRYPT_MODE, secureKey);
        byte[] encrypted = c.doFinal(msg.getBytes());
        return new String(Base64.encodeBase64(encrypted, false));

    }

    /**
     * AES256 : ECB/PKCS5Padding 복호화
     * @param str
     * @return
     * @throws java.io.UnsupportedEncodingException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws InvalidAlgorithmParameterException
     * @throws IllegalBlockSizeException
     * @throws BadPaddingException
     */
    public static String AES256Decode(String str, String secretKey) throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException,
            IllegalBlockSizeException, BadPaddingException {

        byte[] keyData = secretKey.getBytes();
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKey secureKey = new SecretKeySpec(keyData, "AES");
        c.init(Cipher.DECRYPT_MODE, secureKey);
        byte[] byteStr = Base64.decodeBase64(str.getBytes());
        return new String(c.doFinal(byteStr),"UTF-8");
    }

}
