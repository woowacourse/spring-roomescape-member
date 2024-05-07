package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static roomescape.exception.ExceptionType.NOT_FOUND_USER;
import static roomescape.exception.ExceptionType.WRONG_PASSWORD;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.User;
import roomescape.dto.LoginRequest;
import roomescape.exception.RoomescapeException;
import roomescape.repository.CollectionUserRepository;
import roomescape.repository.UserRepository;

class LoginServiceTest {

    private static final JwtGenerator JWT_GENERATOR = new JwtGenerator();
    private UserRepository userRepository;
    private LoginService loginService;

    @BeforeEach
    void init() {
        userRepository = new CollectionUserRepository();
        loginService = new LoginService(userRepository, JWT_GENERATOR);
    }

    @DisplayName("유저 데이터가 존재할 때")
    @Nested
    class UserExistsTest {

        private static final User DEFAULT_USER = new User("name", "email@email.com", "password");

        @BeforeEach
        void addDefaultUser() {
            userRepository.save(DEFAULT_USER);
        }

        @DisplayName("정상적인 로그인에 대해 토큰을 생성할 수 있다.")
        @Test
        void createTokenTest() {
            //when
            String createdToken = loginService.getLoginToken(new LoginRequest(
                    DEFAULT_USER.getEmail(),
                    DEFAULT_USER.getPassword()
            ));

            //then
            Claims payload = JWT_GENERATOR.decodePayload(createdToken);
            assertAll(
                    () -> assertThat(payload.get("email")).isEqualTo(DEFAULT_USER.getEmail()),
                    () -> assertThat(payload.get("name")).isEqualTo(DEFAULT_USER.getName())
            );
        }

        @DisplayName("존재하지 않는 email 요청을 하면 예외가 발생한다.")
        @Test
        void notFoundEmailGetTokenTest() {
            assertThatThrownBy(() -> loginService.getLoginToken(new LoginRequest(
                    DEFAULT_USER.getEmail() + "wrong",
                    DEFAULT_USER.getPassword()
            )))
                    .isInstanceOf(RoomescapeException.class)
                    .hasMessage(NOT_FOUND_USER.getMessage());
        }

        @DisplayName("잘못된 비밀번호로 요청을 하면 예외가 발생한다.")
        @Test
        void illegalPasswordGetTokenTest() {
            assertThatThrownBy(() -> loginService.getLoginToken(new LoginRequest(
                    DEFAULT_USER.getEmail(),
                    DEFAULT_USER.getPassword() + " wrong"
            )))
                    .isInstanceOf(RoomescapeException.class)
                    .hasMessage(WRONG_PASSWORD.getMessage());
        }
    }
}
