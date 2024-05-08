package roomescape.infrastructure;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @DisplayName("토큰을 생성한다.")
    @Test
    void createToken() {
        String token = jwtTokenProvider.createToken("someData");

        assertThat(token).isNotEmpty();
    }

    @DisplayName("payload를 가져온다.")
    @Test
    void getPayLoad() {
        String payload = "someData";

        String token = jwtTokenProvider.createToken(payload);

        assertThat(jwtTokenProvider.getPayLoad(token)).isEqualTo(payload);
    }

    @DisplayName("유효기간이 만료된 토큰을 검증한다.")
    @Test
    void validateExpiredToken() {
        Claims claims = Jwts.claims().setSubject("data");

        Date now = new Date();
        Date validity = new Date(now.getTime());
        String expiredToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, "ssss")
                .compact();

        assertThat(jwtTokenProvider.validateToken(expiredToken)).isFalse();
    }

    @DisplayName("유효기간이 만료되지 않은 토큰을 검증한다.")
    @Test
    void validateUnexpiredToken() {
        String unexpiredToken = jwtTokenProvider.createToken("data");

        assertThat(jwtTokenProvider.validateToken(unexpiredToken)).isTrue();
    }
}