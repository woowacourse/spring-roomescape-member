package roomescape.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.error.UnauthorizedException;
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
        JwtPayload payload = jwtTokenProvider.getPayload(token);

        assertAll(
                () -> assertThat(token).isNotNull(),
                () -> assertThat(payload.memberId()).isEqualTo(id),
                () -> assertThat(payload.name()).isEqualTo(name),
                () -> assertThat(payload.email()).isEqualTo(email)
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

        String expiredToken = JWT.create()
                .withIssuedAt(new Date(now.getTime() - expiration))
                .withExpiresAt(new Date(now.getTime() - expiration))
                .sign(Algorithm.HMAC256(secretKey));

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(expiredToken))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("토큰이 만료되었습니다.");
    }

    @DisplayName("토큰이 올바르지 않은 경우 예외가 발생한다")
    @Test
    void validate_token_signature_exception() {
        // given
        Date now = new Date();

        String invalidSignatureToken = JWT.create()
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + expiration))
                .sign(Algorithm.HMAC256("invalidKey"));

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(invalidSignatureToken))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("토큰 내부 서명이 올바르지 않습니다.");
    }

    @DisplayName("토큰 내부에 값이 존재하지 않으면 예외가 발생한다")
    @Test
    void get_required_claim_test() {
        // given
        Long id = 3L;
        String email = "woowa@woowa.com";
        String password = "woowa123";
        Role role = Role.USER;

        Date now = new Date();
        String token = JWT.create()
                .withSubject(id.toString())
                .withClaim("email", email)
                .withClaim("password", password)
                .withClaim("role", role.toString())
                .withIssuedAt(new Date(now.getTime()))
                .withExpiresAt(new Date(now.getTime() + expiration))
                .sign(Algorithm.HMAC256(secretKey));

        // when & then
        assertThatThrownBy(() -> jwtTokenProvider.getPayload(token))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("JWT에 [name] 클레임이 없습니다.");
    }

}
