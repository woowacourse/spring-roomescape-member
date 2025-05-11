package roomescape.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import roomescape.entity.Member;
import roomescape.entity.Role;

import static org.assertj.core.api.Assertions.assertThat;

class JwtProviderTest {

    private final static String SECRET_KEY = "Yn2kjibddFAWtnPJ2AFlL8WXmohJMCvigQggaEypa5E=";

    private JwtProvider jwtProvider = new JwtProvider(SECRET_KEY);

    @Test
    void 토큰을_생성한다() {
        // given
        Member admin = new Member(1L, "어드민", "admin@email.com", Role.ADMIN);

        // when
        String token = jwtProvider.generateToken(admin);

        // then
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .build()
            .parseClaimsJws(token)
            .getBody();

        assertThat(claims.getSubject()).isEqualTo("1");
        assertThat(claims.get("name")).isEqualTo("어드민");
    }

    @Test
    void 토큰에서_memberId를_추출한다() {
        // given
        Member admin = new Member(1L, "어드민", "admin@email.com", Role.ADMIN);
        String token = jwtProvider.generateToken(admin);
        
        // when
        Long memberId = jwtProvider.extractMemberId(token);

        // then
        assertThat(memberId).isEqualTo(1L);
    }
}