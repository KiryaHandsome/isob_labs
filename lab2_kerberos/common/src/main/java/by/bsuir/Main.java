package by.bsuir;

import by.bsuir.des.EncryptionUtils;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        String message = "Hello world!";
        String secret = "secretkey";

        byte[] encrypted = EncryptionUtils.encrypt(message.getBytes(), secret);
        System.out.println(Arrays.toString(encrypted));

        String decrypted = EncryptionUtils.decrypt(encrypted, secret);
        System.out.println(decrypted);
    }
}