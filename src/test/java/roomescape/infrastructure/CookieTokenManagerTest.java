package roomescape.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CookieTokenManagerTest {

    TokenManager tokenManager;
    HttpServletRequest request;
    HttpServletResponse response;

    @BeforeEach
    void setUp() {
        tokenManager = new CookieTokenManager();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
    }

    @DisplayName("토큰이 존재하면 가져온다.")
    @Test
    void getToken() {
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("token", "1234")});

        String token = tokenManager.getToken(request);

        assertThat(token).isEqualTo("1234");
    }

    @DisplayName("요청에 쿠키 자체가 존재하지 않으면 null을 반환한다.")
    @Test
    void getTokenWithNoCookies() {
        when(request.getCookies()).thenReturn(null);

        String token = tokenManager.getToken(request);

        assertThat(token).isNull();
    }

    @DisplayName("쿠키에 token이 없는 경우 null을 반환한다.")
    @Test
    void getTokenWithNoTokenCookie() {
        when(request.getCookies()).thenReturn(new Cookie[]{new Cookie("token1", "1234")});

        String token = tokenManager.getToken(request);

        assertThat(token).isNull();
    }
}
