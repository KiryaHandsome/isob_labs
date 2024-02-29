package by.bsuir.lab1.cipher;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class CaesarCipherTest {

    @Test
    void encrypt_should_return_expected_string() {
        Cipher cipher = new CaesarCipher(5);
        String input = "Some text to encrypt...";
        String expected = "Xtrj yjcy yt jshwduy...";

        String actual  = cipher.encrypt(input);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void decrypt_should_return_expected_string() {
        Cipher cipher = new CaesarCipher(5);
        String input = "Xtrj yjcy yt ijhwduy...";
        String expected = "Some text to decrypt...";

        String actual  = cipher.decrypt(input);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void encrypt_and_decrypt_should_return_initial_string() {
        Cipher cipher = new CaesarCipher(5);
        String input = "SOMESTRING";

        String encrypted  = cipher.encrypt(input);
        String actual  = cipher.decrypt(encrypted);

        Assertions.assertEquals(input, actual);
    }
}