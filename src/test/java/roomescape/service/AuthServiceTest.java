package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import static roomescape.model.Role.MEMBER;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.exception.BadRequestException;
import roomescape.model.User;
import roomescape.service.fake.FakeJwtTokenProvider;

class AuthServiceTest {

    private FakeJwtTokenProvider jwtTokenProvider = new FakeJwtTokenProvider();
    private AuthService authService = new AuthService(jwtTokenProvider);

    @DisplayName("사용자를 제공하면 쿠키를 만들어 준다.")
    @Test
    void should_create_cookie_when_given_user() {
        User user = new User(1L, "썬", MEMBER, "sun@email.com", "1234");

        Cookie cookie = authService.createCookieByUser(user);

        assertSoftly(softAssertions -> {
            softAssertions.assertThat(cookie.getName()).isEqualTo("token");
            softAssertions.assertThat(cookie.getPath()).isEqualTo("/");
            softAssertions.assertThat(cookie.isHttpOnly()).isTrue();
        });
    }

    @DisplayName("쿠키를 제공하면 쿠키 아이디가 있는지 확인하고 없으면 예외를 발생시킨다.")
    @Test
    void should_throw_exception_when_not_exist_cookie_id() {
        User user = new User(1L, "썬", MEMBER, "sun@email.com", "1234");
        String token = jwtTokenProvider.createToken(user);
        Cookie[] cookies = new Cookie[]{new Cookie("id", token)};

        assertThatThrownBy(() -> authService.findUserNameByCookies(cookies))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 아이디가 token인 쿠키가 없습니다.");
    }

    @DisplayName("쿠키를 제공하면 쿠키 아이디가 있는지 확인하고 있다면 사용자 아이디를 반환한다.")
    @Test
    void should_return_user_id_when_give_cookie() {
        User user = new User(1L, "썬", MEMBER, "sun@email.com", "1234");
        String token = jwtTokenProvider.createToken(user);
        Cookie[] cookies = new Cookie[]{new Cookie("token", token)};

        Long userId = authService.findUserNameByCookies(cookies);

        assertThat(userId).isEqualTo(1L);
    }
}
