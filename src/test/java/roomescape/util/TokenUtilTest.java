package roomescape.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.servlet.http.Cookie;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.exception.AuthenticationException;

class TokenUtilTest {

    TokenProvider jwtProvider = new JwtProvider("woowacoursewoowacoursewoowacourse", 60_000);

    @DisplayName("토큰을 생성하고 값을 추출한다.")
    @Test
    void createAndExtract() {
        TokenUtil tokenUtil = new TokenUtil(jwtProvider);

        String expected = "zeus";
        Cookie cookie = tokenUtil.create(expected);
        Cookie[] cookies = new Cookie[]{cookie};

        String actual = tokenUtil.extractValue(cookies).get();
        assertEquals(expected, actual);
    }

    @DisplayName("만료된 토큰을 생성한다.")
    @Test
    void expired() {
        TokenUtil tokenUtil = new TokenUtil(jwtProvider);

        Cookie cookie = tokenUtil.expired();

        int actual = cookie.getMaxAge();
        assertEquals(0, actual);
    }

    @DisplayName("토큰이 없는 경우 인증 예외가 발생한다.")
    @Test
    void nullCookieException() {
        TokenUtil tokenUtil = new TokenUtil(jwtProvider);
        assertThrows(
                AuthenticationException.class,
                () -> tokenUtil.extractValue(null)
        );
    }
}
