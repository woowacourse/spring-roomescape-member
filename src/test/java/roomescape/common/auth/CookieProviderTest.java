package roomescape.common.auth;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CookieProviderTest {

    private final CookieProvider cookieProvider = new CookieProvider();

    @DisplayName("토큰으로 쿠키를 생성할 수 있다.")
    @Test
    void createCookie() {
        //given
        String name = "token";
        String token = "abcdefg1234";

        //when
        Cookie cookie = cookieProvider.createCookie(name, token);

        //then
        assertThat(cookie).isNotNull();
        assertThat(cookie.getName()).isEqualTo("token");
    }

    @DisplayName("만료된 쿠키를 생성할 수 있다.")
    @Test
    void expireCookie() {
        //given
        String name = "token";

        //when
        Cookie expireCookie = cookieProvider.createExpireCookie(name);

        //then
        assertThat(expireCookie.getMaxAge()).isEqualTo(0);
    }

}
