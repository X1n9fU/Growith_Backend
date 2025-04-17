package dev.book.user_friend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Component
public class AESUtil {
    private static final String ALGORITHM = "AES";

    private static String SECRET_KEY;

    @Value("${spring.aes.secret}")
    public void setSecretKey(String secretKey){
        AESUtil.SECRET_KEY = secretKey;
    }

    public static String encrypt(String input) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] enc = cipher.doFinal(input.getBytes());
        return Base64.getEncoder().encodeToString(enc);
    }

    public static String decrypt(String encrypted) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] dec = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(dec);
    }
}
