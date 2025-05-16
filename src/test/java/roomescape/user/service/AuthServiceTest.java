package roomescape.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.global.exception.RoomEscapeException.AuthenticationException;
import roomescape.global.exception.RoomEscapeException.ResourceNotFoundException;
import roomescape.infra.JwtTokenProvider;
import roomescape.user.dto.request.LoginRequest;
import roomescape.user.dto.response.LoginCheckResponse;
import roomescape.user.repository.UserDao;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles(value = "test")
class AuthServiceTest {

    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserService userService;

    private UserDao userDao;

    @BeforeEach
    void setUp() {
        DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
                .addScript("schema.sql")
                .addScript("data.sql")
                .build();
        userDao = new UserDao(dataSource);
        authService = new AuthService(jwtTokenProvider, userDao, userService);
    }

    @Test
    void 로그인_성공() {
        // given
        String email = "admin1@test.com";
        String password = "test";
        LoginRequest loginRequest = new LoginRequest(email, password);

        // when
        String token = authService.login(loginRequest);

        // then
        assertThat(token).isNotNull();
        assertThat(jwtTokenProvider.getPayload(token)).contains(email);
    }

    @Test
    void 존재하지_않는_이메일로_로그인_시도시_예외발생() {
        // given
        String email = "nonexistent@test.com";
        String password = "test";
        LoginRequest loginRequest = new LoginRequest(email, password);

        // when, then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 비밀번호_불일치시_로그인_실패_예외발생() {
        // given
        String email = "admin@test.com";
        String wrongPassword = "wrongpassword";
        LoginRequest loginRequest = new LoginRequest(email, wrongPassword);

        // when, then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void 유효한_토큰으로_사용자_확인_성공() {
        // given
        String email = "admin1@test.com";
        String token = jwtTokenProvider.createAccessToken(email);

        // when
        LoginCheckResponse response = authService.checkUserByToken(token);

        // then
        assertThat(response).isNotNull();
        assertThat(response.name()).isEqualTo("어드민1");
    }

    @Test
    void 유효하지_않은_토큰으로_사용자_확인시_예외발생() {
        // given
        String invalidToken = "invalid.token";

        // when, then
        assertThatThrownBy(() -> authService.checkUserByToken(invalidToken))
                .isInstanceOf(AuthenticationException.class);
    }
}
