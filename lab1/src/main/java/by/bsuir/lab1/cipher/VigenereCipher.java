package by.bsuir.lab1.cipher;

public class VigenereCipher implements Cipher {

    private final String key;
    private final int alphabetSize;
    private final char uppercaseFirstLetter;
    private final char lowercaseFirstLetter;

    public VigenereCipher(String key) {
        this.key = key;
        this.alphabetSize = 26;
        this.uppercaseFirstLetter = 'A';
        this.lowercaseFirstLetter = 'a';
    }

    @Override
    public String encrypt(String input) {
        StringBuilder encryptedData = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char x = input.charAt(i);
            if (!Character.isLetter(x)) {
                encryptedData.append(x);
                continue;
            }
            char encryptedChar = getEncryptedChar(x, i);
            encryptedData.append(encryptedChar);
        }
        return encryptedData.toString();
    }

    private char getEncryptedChar(char current, int index) {
        char keyChar = key.charAt(index % key.length());
        int shift = (Character.toUpperCase(keyChar) + Character.toUpperCase(current)) % alphabetSize;
        if (Character.isUpperCase(current))
            return (char) (uppercaseFirstLetter + shift);
        else
            return (char) (lowercaseFirstLetter + shift);
    }

    @Override
    public String decrypt(String input) {
        StringBuilder decryptedData = new StringBuilder(input.length());
        for (int i = 0; i < input.length(); i++) {
            char x = input.charAt(i);
            if (!Character.isLetter(x)) {
                decryptedData.append(x);
                continue;
            }
            char decryptedChar = getDecryptedChar(x, i);
            decryptedData.append(decryptedChar);
        }
        return decryptedData.toString();
    }

    private char getDecryptedChar(char current, int index) {
        char keyChar = key.charAt(index % key.length());
        if (Character.isUpperCase(current))
            return (char) (uppercaseFirstLetter + (current - Character.toUpperCase(keyChar) + alphabetSize) % alphabetSize);
        else
            return (char) (lowercaseFirstLetter + (current - Character.toLowerCase(keyChar) + alphabetSize) % alphabetSize);
    }
}
