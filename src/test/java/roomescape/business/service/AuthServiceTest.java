package roomescape.business.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.business.domain.LoginUser;
import roomescape.business.domain.Role;
import roomescape.exception.auth.InvalidCredentialsException;
import roomescape.exception.auth.InvalidTokenException;
import roomescape.presentation.dto.LoginRequest;

@SpringBootTest
class AuthServiceTest {

    @Value("${jwt.secret}")
    private String secretKey;

    private final AuthService authService;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    AuthServiceTest(
            final JdbcTemplate jdbcTemplate,
            final AuthService authService
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.authService = authService;
    }

    @BeforeEach
    void setUp() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS users CASCADE");
        jdbcTemplate.execute("""
                CREATE TABLE users
                  (
                      id   SERIAL,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL UNIQUE,
                      password VARCHAR(255) NOT NULL,
                      role VARCHAR(255) NOT NULL,
                      PRIMARY KEY (id)
                  );
                """);
        jdbcTemplate.update("INSERT INTO USERS (name, email, password, role) values ('hotteok', 'hoho', 'qwe123', 'USER')");
    }

    @DisplayName("토큰을 생성한다.")
    @Test
    void createToken() {
        // given
        final LoginRequest loginRequest = new LoginRequest("hoho", "qwe123");

        // when
        final String token = authService.createToken(loginRequest);
        final Claims actual = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();

        // then
        assertAll(
                () -> assertThat(Long.valueOf(actual.getSubject())).isEqualTo(1L),
                () -> assertThat(actual.get("name", String.class)).isEqualTo("hotteok"),
                () -> assertThat(actual.get("role", String.class)).isEqualTo("USER")
        );
    }

    @DisplayName("회원 정보가 올바르지 않으면 예외가 발생한다.")
    @Test
    void createTokenOrThrow() {
        // given
        final LoginRequest invalidEmailLoginRequest = new LoginRequest("invalid", "qwe123");
        final LoginRequest invalidPasswordLoginRequest = new LoginRequest("hoho", "invalid");

        // when & then
        assertAll(
                () -> assertThatThrownBy(() -> authService.createToken(invalidEmailLoginRequest))
                        .isInstanceOf(InvalidCredentialsException.class),
                () -> assertThatThrownBy(() -> authService.createToken(invalidPasswordLoginRequest))
                        .isInstanceOf(InvalidCredentialsException.class)
        );
    }

    @DisplayName("토큰을 검증하고, 토큰의 유저 정보를 반환한다.")
    @Test
    void verifyTokenAndGetLoginUser() {
        // given
        final LoginRequest loginRequest = new LoginRequest("hoho", "qwe123");
        final String token = authService.createToken(loginRequest);

        // when
        final LoginUser loginUser = authService.verifyTokenAndGetLoginUser(token);

        // then
        assertAll(
                () -> assertThat(loginUser.id()).isEqualTo(1L),
                () -> assertThat(loginUser.name()).isEqualTo("hotteok"),
                () -> assertThat(loginUser.role()).isEqualTo(Role.USER)
        );
    }

    @DisplayName("잘못된 토큰을 검증 시 예외가 발생한다.")
    @Test
    void verifyTokenAndThrowWhenInvalid() {
        // given
        final String invalidToken = "invalidToken";

        // when & then
        assertThatThrownBy(() -> authService.verifyTokenAndGetLoginUser(invalidToken))
                .isInstanceOf(InvalidTokenException.class);
    }
    
}
