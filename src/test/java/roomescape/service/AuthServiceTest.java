package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import roomescape.infrastructure.JwtTokenProvider;
import roomescape.repository.MemberCredentialRepository;
import roomescape.service.dto.login.LoginRequest;

import static org.assertj.core.api.Assertions.assertThat;

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
                new JwtTokenProvider("secret", 360_000L)
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
}
