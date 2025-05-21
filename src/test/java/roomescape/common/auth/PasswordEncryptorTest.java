package roomescape.common.auth;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class PasswordEncryptorTest {

    @DisplayName("비밀번호를 해쉬 암호화 한다.")
    @Test
    void hash() {
        //given
        final String password = "1234";

        //when
        final String actual = PasswordEncryptor.encrypt(password);

        //then
        System.out.println("actual = " + actual);
    }

    @DisplayName("비밀번호가 해쉬화된 비밀번호와 일치한다면 true를 반환한다.")
    @Test
    void matches() {
        //given
        final String password = "1234";
        final String encryptPassword = PasswordEncryptor.encrypt(password);

        //when
        final boolean actual = PasswordEncryptor.matches("1234", encryptPassword);

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("비밀번호가 해쉬화된 비밀번호와 일치하지 않는다면 false를 반환한다.")
    @Test
    void nonMatches() {
        //given
        final String password = "1234";
        final String encryptPassword = PasswordEncryptor.encrypt(password);

        //when
        final boolean actual = PasswordEncryptor.matches("123", encryptPassword);

        //then
        assertThat(actual).isFalse();
    }

}
