package by.bsuir.lab1.cipher;

public class CaesarCipher implements Cipher {

    private final int shift;
    private final int alphabetSize;
    private final char uppercaseFirstLetter;
    private final char lowercaseFirstLetter;

    public CaesarCipher(int shift) {
        this.shift = shift;
        this.alphabetSize = 26;
        this.uppercaseFirstLetter = 'A';
        this.lowercaseFirstLetter = 'a';
    }

    @Override
    public String encrypt(String input) {
        StringBuilder encryptedData = new StringBuilder(input.length());
        for (char x : input.toCharArray()) {
            if (!Character.isLetter(x)) {
                encryptedData.append(x);
                continue;
            }
            char encryptedChar = getEncryptedChar(x);
            encryptedData.append(encryptedChar);
        }
        return encryptedData.toString();
    }

    private char getEncryptedChar(char current) {
        if (Character.isUpperCase(current))
            return (char) (uppercaseFirstLetter + (current - uppercaseFirstLetter + shift) % alphabetSize);
        else
            return (char) (lowercaseFirstLetter + (current - lowercaseFirstLetter + shift) % alphabetSize);
    }

    @Override
    public String decrypt(String input) {
        StringBuilder decryptedData = new StringBuilder(input.length());
        for (char x : input.toCharArray()) {
            if (!Character.isLetter(x)) {
                decryptedData.append(x);
                continue;
            }
            char decryptedChar = getDecryptedChar(x);
            decryptedData.append(decryptedChar);
        }
        return decryptedData.toString();
    }

    private char getDecryptedChar(char current) {
        if (Character.isUpperCase(current))
            return (char) (uppercaseFirstLetter + (current - uppercaseFirstLetter - shift + alphabetSize) % alphabetSize);
        else
            return (char) (lowercaseFirstLetter + (current - lowercaseFirstLetter - shift + alphabetSize) % alphabetSize);
    }
}