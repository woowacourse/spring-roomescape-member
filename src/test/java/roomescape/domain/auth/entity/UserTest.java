package roomescape.domain.auth.entity;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.common.exception.InvalidArgumentException;
import roomescape.domain.auth.exception.InvalidAuthorizationException;

class UserTest {

    @DisplayName("패스워드가 25자 이상이거나 공백이라면 예외를 반환한다.")
    @NullAndEmptySource
    @ParameterizedTest
    @ValueSource(strings = {"aaaaabbbbbcccccdddddeeee26"})
    void passwordTest1(final String password) {
        // given
        final Name name = new Name("꾹");
        final String email = "231321@naver.com";

        // when
        // then
        assertThatThrownBy(() -> {
            new User(1L, name, email, password, Roles.USER);
        }).isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("이메일이 형식에 맞지 않다면 예외를 반환한다.")
    @NullAndEmptySource
    @ParameterizedTest
    @ValueSource(strings = {"@naver.com", "tdasdsad@", "sdadsa@naver"})
    void emailTest(final String email) {
        // given
        final Name name = new Name("꾹");
        final String password = "123456";

        // when
        // then
        assertThatThrownBy(() -> {
            new User(1L, name, email, password, Roles.USER);
        }).isInstanceOf(InvalidArgumentException.class);
    }

    @DisplayName("유저 생성 성공 테스트")
    @Test
    void userTest1() {
        // given
        final String email = "tizm@naver.com";
        final String password = "123456";
        final Name name = new Name("꾹");

        // when
        // then
        assertThatCode(() -> {
            new User(1L, name, email, password, Roles.USER);
        }).doesNotThrowAnyException();
    }

    @DisplayName("로그인 값이 다르다면 예외를 반환한다")
    @Test
    void login_throwsException() {
        // given
        final String email = "sdaas@naver.com";
        final User user = new User(1L, new Name("꾹"), email, "1234", Roles.USER);

        // when & then
        assertThatThrownBy(() -> {
            user.login(email, "123");
        }).isInstanceOf(InvalidAuthorizationException.class);
    }

}
