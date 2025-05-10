package roomescape.domain.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import roomescape.domain.auth.dto.LoginUserDto;
import roomescape.domain.auth.dto.UserInfoResponse;
import roomescape.domain.auth.entity.Name;
import roomescape.domain.auth.entity.Roles;
import roomescape.domain.auth.entity.User;
import roomescape.domain.auth.exception.InvalidAuthorizationException;
import roomescape.domain.auth.repository.UserRepository;

@SpringBootTest
@Transactional
public class AuthServiceIntegrationTest {

    @Autowired
    private JwtManager jwtManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @DisplayName("유저 정보 조회")
    @Nested
    class userInfo {
        @DisplayName("토큰을 검증하여 유저 정보를 반환한다.")
        @Test
        void userInfoTest() {
            // given
            final Name name = new Name("꾹");
            final String email = "asdad@naver.com";

            final User savedUser = userRepository.save(User.withoutId(name, email, "1234", Roles.USER));
            final UserInfoResponse expected = UserInfoResponse.from(savedUser);
            final LoginUserDto loginUserDto = new LoginUserDto(savedUser.getId(), savedUser.getRole());

            // when
            final UserInfoResponse result = authService.getUserInfo(loginUserDto);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @DisplayName("존재하지 않는 id의 경우 예외를 반환한다.")
        @Test
        void userInfo_throwsException() {
            // given
            final LoginUserDto loginUserDto = new LoginUserDto(210L, Roles.USER);

            // when & then
            assertThatThrownBy(() -> {
                authService.getUserInfo(loginUserDto);
            }).isInstanceOf(InvalidAuthorizationException.class);
        }
    }

    @DisplayName("로그인 정보 조회")
    @Nested
    class loginInfo {
        @DisplayName("토큰을 검증하여 유저 정보를 반환한다.")
        @Test
        void loginUserByTokenTest1() {
            // given
            final Name name = new Name("꾹");
            final String email = "asdad@naver.com";

            final User savedUser = userRepository.save(User.withoutId(name, email, "1234", Roles.USER));
            final String token = jwtManager.createToken(savedUser);
            final LoginUserDto expected = LoginUserDto.from(savedUser);

            // when
            final LoginUserDto result = authService.getLoginUser(token);

            // then
            assertThat(result).isEqualTo(expected);
        }

        @DisplayName("잘못된 토큰이라면 InvalidAuthorizationException을 반환한다.")
        @Test
        void loginUser_throwsException() {
            // given
            final String invalidToken = "213123213214sdadcxzcxz";

            // when & then
            assertThatThrownBy(() -> {
                authService.getLoginUser(invalidToken);
            }).isInstanceOf(InvalidAuthorizationException.class);
        }
    }
}
