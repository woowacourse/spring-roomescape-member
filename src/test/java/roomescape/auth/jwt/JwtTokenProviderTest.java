package roomescape.auth.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;

class JwtTokenProviderTest {

    private final String secretKey = "mytestsecretkey1234567890123456"; // 최소 256비트(32글자 이상) 권장
    private final long expirationMillis = 3600000;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, expirationMillis);
    private final Algorithm algorithm = Algorithm.HMAC256(secretKey); // 검증용

    @DisplayName("Member 정보로 JWT 토큰을 생성한다")
    @Test
    void createTokenFromMemberTest() {
        // given
        Member member = new Member(1L, Role.ADMIN, "어드민", "admin@email.com", "password");

        // when
        String token = jwtTokenProvider.createTokenFromMember(member);
        DecodedJWT decoded = JWT.require(algorithm)
                .build()
                .verify(token);

        // then
        assertThat(decoded.getSubject()).isEqualTo("1");
        assertThat(decoded.getClaim("name").asString()).isEqualTo("어드민");
        assertThat(decoded.getClaim("role").asString()).isEqualTo("ADMIN");
        assertThat(decoded.getExpiresAt()).isAfter(new Date());
    }
}
