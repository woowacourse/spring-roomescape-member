package roomescape.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;

class JwtTokenProviderTest {

    private final String secretKey = "mySecretKey";
    private final Long expiration = 3600000L;
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider(secretKey, expiration);

    @DisplayName("토큰을 생성한다")
    @Test
    void create_token_test() {
        // given
        Long id = 3L;
        String name = "행성이";
        String email = "woowa@woowa.com";
        String password = "woowa123";
        Role role = Role.USER;

        Member member = new Member(id, name, email, password, role);

        // when
        String token = jwtTokenProvider.createToken(member);

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

    @DisplayName("토큰의 데이터를 반환한다")
    @Test
    void get_subject_test() {
        // given
        Long id = 3L;
        String name = "행성이";
        String email = "woowa@woowa.com";
        String password = "woowa123";
        Role role = Role.USER;

        Member member = new Member(id, name, email, password, role);
        String token = jwtTokenProvider.createToken(member);

        // when
        JwtPayload payload = jwtTokenProvider.getPayload(token);

        // then
        assertAll(
                () -> assertThat(payload.memberId()).isEqualTo(id),
                () -> assertThat(payload.name()).isEqualTo(name),
                () -> assertThat(payload.email()).isEqualTo(email),
                () -> assertThat(payload.role()).isEqualTo(role)
        );
    }

    @DisplayName("토큰이 만료되었으면 예외가 발생한다")
    @Test
    void validate_token_expiration_exception() {
        // given
        Date now = new Date();
        String expiredToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setIssuedAt(new Date(now.getTime() - expiration))
                .setExpiration(new Date(now.getTime() - expiration))
                .compact();

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.getPayload(expiredToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("만료된 토큰 입니다.");
    }

    @DisplayName("토큰이 올바르지 않은 경우 예외가 발생한다")
    @Test
    void validate_token_signature_exception() {
        // given
        Date now = new Date();

        String invalidSignatureToken = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, "invalidKey")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + expiration))
                .compact();

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.getPayload(invalidSignatureToken))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }

}
