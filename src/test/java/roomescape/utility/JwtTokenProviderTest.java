package roomescape.utility;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class JwtTokenProviderTest {

    private static final String secretKey = "test_secret_key";
    private static final long validityInMilliseconds = 72000;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, validityInMilliseconds);

    @DisplayName("Access 토큰을 생성할 수 있다")
    @Test
    void canMakeAccessToken() {
        // when
        String accessToken = jwtTokenProvider.makeAccessToken(1L, "이름");

        // then
        Date validity = new Date(new Date().getTime() + validityInMilliseconds);
        Claims tokenBody = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(accessToken).getBody();
        assertAll(
                () -> assertThat(tokenBody.getSubject()).isEqualTo("1"),
                () -> assertThat(tokenBody.get("name")).isEqualTo("이름"),
                () -> assertThat(tokenBody.getExpiration().getTime())
                        .isBetween(validity.getTime() - 60000, validity.getTime())
        );
    }
}
