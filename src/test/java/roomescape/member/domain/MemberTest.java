package roomescape.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.BadRequestException;
import roomescape.exception.ExceptionCause;

class MemberTest {

    @DisplayName("비밀번호는 8자 ~ 16자이고 특수문자, 숫자, 소문자, 대문자 중 3개 이상을 가지고 있어야한다.")
    @ParameterizedTest
    @ValueSource(strings = {"qwer1234!", "abcdABCD123", "abcdefg123!", "abcdefghij12345!"})
    void validatePassword(String password) {

        // when & then
        assertThatCode(() -> new Member(1L, "test", "test@test.com", password, Role.USER))
                .doesNotThrowAnyException();
    }

    @DisplayName("비밀번호는 8자 ~ 16자이고 특수문자, 숫자, 소문자, 대문자 중 3개 이상을 가지고 있지않으면 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"1234567", "aaaabbbbccccdddd123!", "abcdabcd123", "abcdefg!!!!"})
    void validatePasswordThrowsException(String password) {

        // when & then
        assertThatThrownBy(() -> new Member(1L, "test", "test@test.com", password, Role.USER))
                .isInstanceOf(BadRequestException.class)
                .hasMessage(ExceptionCause.MEMBER_PASSWORD_INVALID.getMessage());
    }
}