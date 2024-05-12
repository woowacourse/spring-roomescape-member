package roomescape.infrastructure;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.infrastructure.auth.Token;

import static org.assertj.core.api.Assertions.assertThat;

class CookieManagerTest {

    @Test
    @DisplayName("전달한 토큰을 담은 쿠키를 만든다")
    void should_GenerateCookie_When_GiveCookie() {
        //given
        CookieManager cookieManager = new CookieManager();
        Token token = new Token("testToken");

        //when
        Cookie cookie = cookieManager.generate(token);

        //then
        assertThat(cookie.getValue()).isEqualTo("testToken");
    }

    @Test
    @DisplayName("쿠키 안의 토큰을 가져올 수 있다")
    void should_GetToken_When_GiveCookies() {
        //given
        CookieManager cookieManager = new CookieManager();
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("token", "testToken");

        //when
        Token token = cookieManager.getToken(cookies);

        //then
        assertThat(token.getToken()).isEqualTo(cookies[0].getValue());
    }

    @Test
    @DisplayName("토큰이 담긴 쿠키를 만료시킨다.")
    void should_makeCookie_Reset() {
        //given
        CookieManager cookieManager = new CookieManager();
        Cookie[] cookies = new Cookie[1];
        cookies[0] = new Cookie("token", "testToken");

        //when
        Cookie resetCookie = cookieManager.makeResetCookie(cookies);

        //then
        assertThat(resetCookie.getMaxAge()).isZero();
    }
}