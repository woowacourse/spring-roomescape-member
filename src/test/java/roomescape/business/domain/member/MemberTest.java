package roomescape.business.domain.member;

import static org.assertj.core.api.Assertions.assertThatCode;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.MemberException;

class MemberTest {

    private static final String VALID_NAME = "벨로";
    private static final String VALID_EMAIL = "bello@email.com";
    private static final String VALID_PASSWORD = "password";

    @DisplayName("사용자의 이름이 null이거나 빈 문자열인 경우에 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenNameIsNullOrEmpty(String invalidName) {
        // given
        // when
        // then
        assertThatCode(() -> new Member(1L, invalidName, VALID_EMAIL, VALID_PASSWORD))
                .isInstanceOf(MemberException.class)
                .hasMessage("사용자 이름은 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("사용자의 이름이 2자 이상 8자 이하가 아닌 경우에 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"a", "abcdefghi"})
    void shouldThrowExceptionWhenNameIsNotInValidLength(String invalidName) {
        // given
        // when
        // then
        assertThatCode(() -> new Member(1L, invalidName, VALID_EMAIL, VALID_PASSWORD))
                .isInstanceOf(MemberException.class)
                .hasMessage("사용자 이름은 2자 이상 8자 이하이어야 합니다.");
    }

    @DisplayName("사용자의 이름이 2자 이상 8자 이하인 경우에 예외가 발생하지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {"ab", "abcdefgh"})
    void shouldNotThrowExceptionWhenNameIsInValidLength(String validName) {
        // given
        // when
        // then
        assertThatCode(() -> new Member(1L, validName, VALID_EMAIL, VALID_PASSWORD))
                .doesNotThrowAnyException();
    }

    @DisplayName("사용자의 이메일이 null이거나 빈 문자열인 경우에 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void shouldThrowExceptionWhenEmailIsNullOrEmpty(String invalidEmail) {
        // given
        // when
        // then
        assertThatCode(() -> new Member(1L, VALID_NAME, invalidEmail, VALID_PASSWORD))
                .isInstanceOf(MemberException.class)
                .hasMessage("이메일은 null이거나 빈 문자열일 수 없습니다.");
    }

    @DisplayName("사용자의 이메일 형식이 올바르지 않은 경우에 예외가 발생한다.")
    @ParameterizedTest
    @ValueSource(strings = {"invalidEmail", "invalid@Email", "invalid@Email."})
    void shouldThrowExceptionWhenEmailIsInvalidFormat(String invalidEmail) {
        // given
        // when
        // then
        assertThatCode(() -> new Member(1L, VALID_NAME, invalidEmail, VALID_PASSWORD))
                .isInstanceOf(MemberException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
    }

//    @DisplayName("사용자의 비밀번호가 null이거나 빈 문자열인 경우에 예외가 발생한다.")
//    @ParameterizedTest
//    @NullAndEmptySource
//    void shouldThrowExceptionWhenPasswordIsNullOrEmpty(String invalidPassword) {
//        // given
//        // when
//        // then
//        assertThatCode(() -> new Member(1L, VALID_NAME, VALID_EMAIL, invalidPassword))
//                .isInstanceOf(IllegalArgumentException.class)
//                .hasMessage("비밀번호는 null이거나 빈 문자열일 수 없습니다.");
//    }
}
