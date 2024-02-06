package by.bsuir.lab1.cipher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VigenereCipherTest {

    @Test
    void encrypt_should_return_expected_string() {
        String key = "LEMON";
        String input = "AttackAtDawn";
        String expected = "LxfopvEfRnhr";
        Cipher cipher = new VigenereCipher(key);

        String actual = cipher.encrypt(input);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void decrypt_should_return_expected_string() {
        String key = "LEMON";
        String input = "LxfopvEfRnhr";
        String expected = "AttackAtDawn";
        Cipher cipher = new VigenereCipher(key);

        String actual = cipher.decrypt(input);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void encrypt_and_decrypt_should_return_initial_string() {
        String key = "LEMON";
        Cipher cipher = new VigenereCipher(key);
        String input = "SomeString";

        String encrypted  = cipher.encrypt(input);
        String actual  = cipher.decrypt(encrypted);

        Assertions.assertEquals(input, actual);
    }
}