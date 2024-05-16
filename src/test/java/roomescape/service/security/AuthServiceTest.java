package roomescape.service.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.domain.member.Member;
import roomescape.domain.member.Role;
import roomescape.exception.AuthorizationException;
import roomescape.infrastructure.JwtTokenHandler;
import roomescape.repository.member.MemberCredentialRepository;
import roomescape.service.dto.login.LoginRequest;

@JdbcTest
@Sql("/truncate-data.sql")
class AuthServiceTest {

    private JdbcTemplate jdbcTemplate;
    private AuthService authService;

    @Autowired
    public AuthServiceTest(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        authService = new AuthService(
                new MemberCredentialRepository(jdbcTemplate),
                new JwtTokenHandler("secret", 360_000L)
        );
    }

    @Test
    @DisplayName("유효한 id/pw로 로그인 시 토큰을 발급 받을 수 있다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void checkLogin_Success() {
        // given
        LoginRequest request = new LoginRequest("naknak@example.com", "nak123");

        // when
        String token = authService.createToken(request);

        // then
        assertThat(token).isNotBlank();
    }

    @Test
    @DisplayName("유효하지 않은 id/pw로 로그인 시 예외가 발생한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void checkLogin_Failure() {
        // given
        LoginRequest request = new LoginRequest("slkfj@example.com", "sfd");

        // when & then
        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("유효하지 않은 id/pw 입니다.");
    }

    @Test
    @DisplayName("유효한 토큰으로 사용자 인증 정보를 조회할 수 있다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void auth_Success() {
        // given
        LoginRequest request = new LoginRequest("naknak@example.com", "nak123");
        String token = authService.createToken(request);

        // when
        Member member = authService.findMemberByValidToken(token);

        // then
        assertThat(member)
                .usingRecursiveComparison()
                .isEqualTo(new Member(2L, "naknak", Role.MEMBER));
    }

    @Test
    @DisplayName("유효하지 않은 토큰으로 인증 정보를 조회하면 예외가 발생한다.")
    @Sql(scripts = {"/truncate-data.sql", "/theme-time-member-data.sql"})
    void auth_Failure() {
        // given
        LoginRequest request = new LoginRequest("naknak@example.com", "nak123");
        String token = authService.createToken(request) + "fsfdf";

        // when & then
        assertThatThrownBy(() -> authService.findMemberByValidToken(token))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage("인증 정보를 확인할 수 없습니다.");
    }
}
