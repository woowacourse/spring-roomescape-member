package roomescape.auth;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class CookieProviderTest {

    @Test
    @DisplayName("토큰 값을 알 수 있다.")
    void extractTokenFrom() {
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.setCookies(new Cookie("token", "1234"));

        final String token = CookieProvider.extractTokenFrom(mockHttpServletRequest);

        assertThat(token).isEqualTo("1234");
    }

    @Test
    void addTokenCookie() {
        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        CookieProvider.addTokenCookie(mockHttpServletResponse, "1234");

        final Cookie cookie = Arrays.stream(mockHttpServletResponse.getCookies())
                .filter(it -> it.getName().equals("token"))
                .findFirst()
                .get();

        assertThat(cookie.getValue()).isEqualTo("1234");
    }

    @Test
    void expireTokenCookie() {
        final MockHttpServletResponse mockHttpServletResponse = new MockHttpServletResponse();
        CookieProvider.expireTokenCookie(mockHttpServletResponse);

        final Cookie cookie = Arrays.stream(mockHttpServletResponse.getCookies())
                .filter(it -> it.getName().equals("token"))
                .findFirst()
                .get();

        assertThat(cookie.getValue()).isNull();
    }
}
