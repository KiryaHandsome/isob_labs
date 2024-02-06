package by.bsuir.lab1;

import by.bsuir.lab1.cipher.CaesarCipher;
import by.bsuir.lab1.cipher.Cipher;
import by.bsuir.lab1.cipher.VigenereCipher;
import by.bsuir.lab1.util.FileUtil;

public class Main {

    public static void main(String[] args) {
        String filePath = "/media/kirya/linux/projects/bsuir/isob_labs/lab1/input.txt";
        String input = FileUtil.readFromFile(filePath);

        Cipher caesarCipher = new CaesarCipher(5);
        String encryptedCaesar = caesarCipher.encrypt(input);
        String decryptedCaesar = caesarCipher.decrypt(encryptedCaesar);

        System.out.println("Encrypted caesar: " + encryptedCaesar);
        System.out.println("Decrypted caesar: " + decryptedCaesar);

        Cipher vigenereCipher = new VigenereCipher("LEMON");
        String encryptedVigenere = vigenereCipher.encrypt(input);
        String decryptedVigenere = vigenereCipher.decrypt(encryptedVigenere);

        System.out.println("Encrypted vigenere: " + encryptedVigenere);
        System.out.println("Decrypted vigenere: " + decryptedVigenere);
    }
}
