package roomescape.auth.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordEncryptorTest {

    @DisplayName("암호화된 비밀번호를 생성할 수 있다.")
    @Test
    void encrypt() {
        PasswordEncryptor passwordEncryptor = new PasswordEncryptor();
        String encryptedPassword = passwordEncryptor.encrypt("1234");
        assertThat(passwordEncryptor.encrypt("1234")).isEqualTo(encryptedPassword);
    }
}
