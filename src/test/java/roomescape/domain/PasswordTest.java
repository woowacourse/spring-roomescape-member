package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidClientRequestException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PasswordTest {

    @DisplayName("패스워드 생성 시 평문 값이 비어있을 경우 오류를 반환한다.")
    @Test
    void given_plainPassword_when_newWithEmptyValue_then_thrownException() {
        assertThatThrownBy(() -> new Password("")).isInstanceOf(InvalidClientRequestException.class);
    }

    @DisplayName("패스워드 생성 시 평문 값이 비어있을 경우 오류를 반환한다.")
    @Test
    void given_plainPassword_when_getHash_then_notEqualPlainPassword() {
        //given
        String plainPassword = "password";
        //when
        final Password password = new Password(plainPassword);
        //then
        assertThat(password.getHashValue()).isNotEqualTo(plainPassword);
    }

    @DisplayName("salt값이 동일한 패스워드의 해시값은 동일하다.")
    @Test
    void given_twoPasswordWithSameSalt_when_getHash_then_equal() {
        //given
        Password password1 = new Password("password", "salt");
        Password password2 = new Password("password", "salt");
        //when, then
        assertThat(password1.getHashValue()).isEqualTo(password2.getHashValue());
    }
}