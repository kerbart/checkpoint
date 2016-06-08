package com.kerbart.checkpoint.services;

import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@Service
public class EncryptService {

    @Value("${app.crypto.key}")
    private String cryptoKey;

    @Value("${app.crypto.salt}")
    private String cryptoSalt;

    public String encryptPassword(String password) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(cryptoKey.toCharArray()));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(toByteArray(cryptoSalt), 20));
            byte[] bytes = password.getBytes("UTF-8");
            byte[] finalBytes = pbeCipher.doFinal(bytes);
            return base64Encode(finalBytes);
        } catch (Exception e) {
            return null;
        }
    }

    public String decryptPassword(String encryptedPassword) {
        try {
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey key = keyFactory.generateSecret(new PBEKeySpec(cryptoKey.toCharArray()));
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(toByteArray(cryptoSalt), 20));
            byte[] bytes = base64Decode(encryptedPassword);
            byte[] finalBytes = pbeCipher.doFinal(bytes);
            return new String(finalBytes, "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

    private static String base64Encode(byte[] bytes) {
        return new BASE64Encoder().encode(bytes);
    }

    private static byte[] base64Decode(String property) throws IOException {
        return new BASE64Decoder().decodeBuffer(property);
    }

    public static byte[] toByteArray(String s) {
        return DatatypeConverter.parseHexBinary(s);
    }
}
