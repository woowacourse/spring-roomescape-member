package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.member.model.Role;
import roomescape.global.auth.JwtProvider;
import roomescape.global.auth.JwtRequest;
import roomescape.global.exception.AuthorizedException;

public class JwtProviderTest {

    private JwtProvider jwtProvider;
    private static final Long TEST_ID = 1L;
    private static final String TEST_NAME = "Test User";
    private static final Role TEST_ROLE = Role.USER;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider();
    }

    @Test
    @DisplayName("JwtRequest로 토큰을 생성할 수 있다")
    void createToken_creates_valid_token() {
        // given
        JwtRequest jwtRequest = new JwtRequest(TEST_ID, TEST_NAME, TEST_ROLE);

        // when
        String token = jwtProvider.createToken(jwtRequest);

        // then
        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
    }

    @Test
    @DisplayName("생성된 토큰을 검증하면 Claims를 반환한다")
    void validateToken_returns_claims_for_valid_token() {
        // given
        JwtRequest jwtRequest = new JwtRequest(TEST_ID, TEST_NAME, TEST_ROLE);
        String token = jwtProvider.createToken(jwtRequest);

        // when
        Claims claims = jwtProvider.validateToken(token);

        // then
        assertThat(claims).isNotNull();
        assertThat(claims.getSubject()).isEqualTo(TEST_ID.toString());
        assertThat(claims.get("name")).isEqualTo(TEST_NAME);
        assertThat(claims.get("role")).isEqualTo(TEST_ROLE.name());
    }

    @Test
    @DisplayName("유효하지 않은 토큰을 검증하면 예외가 발생한다")
    void validateToken_throws_exception_for_invalid_token() {
        // given
        String invalidToken = "invalid.token.string";

        // when & then
        assertThatThrownBy(() -> jwtProvider.validateToken(invalidToken))
            .isInstanceOf(AuthorizedException.class);
    }

    @Test
    @DisplayName("토큰에서 회원 역할을 추출할 수 있다")
    void getMemberRole_extracts_role_from_token() {
        // given
        JwtRequest jwtRequest = new JwtRequest(TEST_ID, TEST_NAME, TEST_ROLE);
        String token = jwtProvider.createToken(jwtRequest);

        // when
        Role role = jwtProvider.getMemberRole(token);

        // then
        assertThat(role).isEqualTo(TEST_ROLE);
    }

    @Test
    @DisplayName("ADMIN 역할을 가진 토큰에서 역할을 추출할 수 있다")
    void getMemberRole_extracts_admin_role_from_token() {
        // given
        JwtRequest jwtRequest = new JwtRequest(TEST_ID, TEST_NAME, Role.ADMIN);
        String token = jwtProvider.createToken(jwtRequest);

        // when
        Role role = jwtProvider.getMemberRole(token);

        // then
        assertThat(role).isEqualTo(Role.ADMIN);
    }
}