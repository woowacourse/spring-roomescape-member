package roomescape.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.constant.Role;
import roomescape.domain.Member;
import roomescape.exception.AuthorizationException;

@SpringBootTest
class JwtTokenProviderTest {

    @Autowired
    JwtTokenProvider tokenProvider;

    JwtTokenProviderTest() {
    }

    @DisplayName("토큰을 받아 사용자의 id값을 반환한다.")
    @Test
    void getIdFromToken() {
        String token = tokenProvider.createToken(new Member(
                1L, "name", "email", "password", Role.ADMIN
        ));

        assertThat(tokenProvider.getIdFromToken(token))
                .isEqualTo(1L);
    }

    @DisplayName("토큰을 받아 사용자의 Role을 반환한다.")
    @Test
    void getRoleFromToken() {
        String token = tokenProvider.createToken(new Member(
                1L, "name", "email", "password", Role.ADMIN
        ));

        assertThat(tokenProvider.getRoleFromToken(token))
                .isEqualTo(Role.ADMIN.name());
    }

    @DisplayName("쿠키에서 토큰을 추출해 반환한다.")
    @Test
    void extractTokenFromCookie() {
        String token = tokenProvider.createToken(new Member(
                1L, "name", "email", "password", Role.ADMIN
        ));

        Cookie cookie = new Cookie(JwtTokenProvider.TOKEN_NAME, token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        Cookie[] cookies = new Cookie[]{cookie};

        assertThat(tokenProvider.extractTokenFromCookie(cookies))
                .isEqualTo(token);
    }

    @DisplayName("쿠키가 비어 있으면 권한 예외로 처리한다.")
    @Test
    void extractTokenFromCookieFailed() {
        assertThatThrownBy(() -> tokenProvider.extractTokenFromCookie(null))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("접근 권한이 없습니다.");
    }
}
