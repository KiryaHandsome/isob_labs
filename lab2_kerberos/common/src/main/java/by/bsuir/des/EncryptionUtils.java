package by.bsuir.des;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.spec.KeySpec;

public class EncryptionUtils {

    public static byte[] encrypt(byte[] data, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            KeySpec keySpec = new DESKeySpec(secretKey.getBytes());
            SecretKey key = keyFactory.generateSecret(keySpec);

            cipher.init(Cipher.ENCRYPT_MODE, key);

            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public static String decrypt(byte[] data, String secretKey) {
        try {
            Cipher cipher = Cipher.getInstance("DES");
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            KeySpec keySpec = new DESKeySpec(secretKey.getBytes());
            SecretKey key = keyFactory.generateSecret(keySpec);

            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decryptedData = cipher.doFinal(data);
            return new String(decryptedData);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}