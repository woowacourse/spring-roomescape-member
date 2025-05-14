package roomescape.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CookieUtilityTest {

    private final CookieUtility cookieUtility = new CookieUtility();

    @DisplayName("요청메세지에서 특정 쿠키의 값을 찾아올 수 있다")
    @Test
    void canFindCookie() {
        // given
        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = {
                new Cookie("key1", "value1"),
                new Cookie("key2", "value2"),
                new Cookie("key3", "value3")};
        when(request.getCookies()).thenReturn(cookies);

        // when
        Optional<Cookie> cookie = cookieUtility.findCookie(request, "key1");

        // then
        assertAll(
                () -> assertThat(cookie.isPresent()).isTrue(),
                () -> assertThat(cookie.get().getValue()).isEqualTo("value1")
        );
    }

}
