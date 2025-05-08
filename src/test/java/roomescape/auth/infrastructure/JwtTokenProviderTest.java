package roomescape.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.domain.User;

class JwtTokenProviderTest {

    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("mySecretKey", 3600000L);

    @DisplayName("토큰을 생성한다")
    @Test
    void create_token_test() {
        // given
        Long id = 3L;
        String name = "행성이";
        String email = "woowa@woowa.com";
        String password = "woowa123";

        User user = new User(id, name, email, password);

        // when
        String token = jwtTokenProvider.createToken(user);

        // then
        Claims claims = Jwts.parser()
                .setSigningKey("mySecretKey")
                .parseClaimsJws(token)
                .getBody();

        assertAll(
                () -> assertThat(token).isNotNull(),
                () -> assertThat(claims.get("sub")).isEqualTo("3"),
                () -> assertThat(claims.get("name")).isEqualTo("행성이"),
                () -> assertThat(claims.get("email")).isEqualTo("woowa@woowa.com")
        );

    }

}
