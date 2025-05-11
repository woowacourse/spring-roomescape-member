package roomescape.auth.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseCookie;
import roomescape.exception.custom.AuthorizationException;

class CookieHandlerTest {

    @DisplayName("토큰으로부터 쿠키를 생성한다")
    @Test
    void createCookieFromTokenTest() {
        String token = "sample-token";

        ResponseCookie cookie = CookieHandler.createCookieFromToken(token);

        assertAll(
                () -> assertThat(cookie.getName()).isEqualTo("token"),
                () -> assertThat(cookie.getValue()).isEqualTo(token),
                () -> assertThat(cookie.isHttpOnly()).isTrue(),
                () -> assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(3600),
                () -> assertThat(cookie.getPath()).isEqualTo("/"),
                () -> assertThat(cookie.getSameSite()).isEqualTo("Lax"),
                () -> assertThat(cookie.isSecure()).isFalse()
        );
    }

    @DisplayName("로그아웃 쿠키를 생성한다")
    @Test
    void createLogoutCookieTest() {
        // when
        ResponseCookie cookie = CookieHandler.createLogoutCookie();

        // then
        assertThat(cookie.getMaxAge().getSeconds()).isEqualTo(0);
        assertThat(cookie.getPath()).isEqualTo("/");
    }

    @DisplayName("쿠키 배열에서 토큰 값을 추출한다")
    @Test
    void extractTokenFromCookiesTest() {
        // given
        Cookie[] cookies = new Cookie[]{
                new Cookie("token", "my-token"),
                new Cookie("example", "value")
        };

        // when
        String token = CookieHandler.extractTokenFromCookies(cookies);

        // then
        assertThat(token).isEqualTo("my-token");
    }

    @DisplayName("쿠키가 null이면 예외를 던진다")
    @Test
    void extractTokenFromCookiesTest_WhenCookiesAreNull() {
        // when // then
        assertThatThrownBy(() -> CookieHandler.extractTokenFromCookies(null))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("쿠키가 존재하지 않습니다");
    }

    @DisplayName("쿠키에 토큰이 없으면 빈 문자열을 반환한다")
    @Test
    void extractTokenFromCookiesTest_WhenTokenNotExists() {
        // given
        Cookie[] cookies = new Cookie[]{new Cookie("example", "value")};

        // when
        String token = CookieHandler.extractTokenFromCookies(cookies);

        // then
        assertThat(token).isEmpty();
    }
}
