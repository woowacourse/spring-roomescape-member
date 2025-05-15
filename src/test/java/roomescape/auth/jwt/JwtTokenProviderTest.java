package roomescape.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

class JwtTokenProviderTest {

    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private final String base64SecretKey = Base64.getEncoder().encodeToString(key.getEncoded());
    private final long expirationMillis = 3600000; // 1시간
    private final JwtTokenProvider jwtTokenProvider
            = new JwtTokenProvider(base64SecretKey, expirationMillis);

    @DisplayName("Member 정보로 JWT 토큰을 생성한다")
    @Test
    void createTokenFromMemberTest() {
        Member member = new Member(1L, Role.ADMIN, "어드민", "admin@email.com", "password");

        String token = jwtTokenProvider.createTokenFromMember(member);

        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.get("name")).isEqualTo("어드민");
        assertThat(claims.get("role")).isEqualTo("ADMIN");
        assertThat(claims.getExpiration()).isAfter(new Date());
    }
}
