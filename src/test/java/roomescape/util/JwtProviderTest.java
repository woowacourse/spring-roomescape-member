package roomescape.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.jsonwebtoken.ExpiredJwtException;

class JwtProviderTest {

    @DisplayName("JWT를 생성하고 subject를 추출한다.")
    @Test
    void createAndExtract() {
        String key = "woowacoursewoowacoursewoowacourse";
        long oneMinuteInMilliseconds = 60 * 1000;
        JwtProvider jwtProvider = new JwtProvider(key, oneMinuteInMilliseconds);

        String expected = "zeus";
        String token = jwtProvider.create(expected);

        String actual = jwtProvider.extract(token);
        assertEquals(expected, actual);
    }

    @DisplayName("만료된 토큰에서 값을 추출하면 예외가 발생한다.")
    @Test
    void extractExpiredJwtException() {
        String key = "woowacoursewoowacoursewoowacourse";
        JwtProvider jwtProvider = new JwtProvider(key, 0);

        String expected = "zeus";
        String token = jwtProvider.create(expected);

        assertThrows(
                ExpiredJwtException.class,
                () -> jwtProvider.extract(token)
        );
    }
}
