package roomescape.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PasswordEncryptorTest {

    @DisplayName("비밀번호를 암호화한다.")
    @Test
    void encrypt() {
        // given
        String rawPassword = "password";

        // when
        String encryptedPassword = PasswordEncryptor.encrypt(rawPassword);

        // then
        assertThat(encryptedPassword)
                .isNotBlank()
                .isNotEqualTo(rawPassword);
    }

    @DisplayName("비밀번호를 비교한다.")
    @ParameterizedTest
    @CsvSource({
            "password, bziTlUMky2GC3ji0qgiFVA==, true",
            "password, wrongPassword, false",
    })
    void matches(String rawPassword, String encryptedPassword, boolean expected) {
        // given
        // when
        boolean isMatch = PasswordEncryptor.matches(rawPassword, encryptedPassword);

        // then
        assertThat(isMatch)
                .isEqualTo(expected);
    }
}
