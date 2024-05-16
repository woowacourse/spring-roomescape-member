package roomescape.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.dto.LoginRequest;
import roomescape.global.infra.JwtTokenProvider;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AuthServiceIntegrationTest {

  @Autowired
  AuthService authService;
  @Autowired
  JwtTokenProvider jwtTokenProvider;

  @DisplayName("유저 이메일로 토큰을 생성한다.")
  @Test
  void createUser() {
    // Given
    final String email = "kelly@example.com";
    final LoginRequest loginRequest = new LoginRequest(email, "password123");
    // When
    final String token = authService.login(loginRequest);
    // Then
    assertThat(email).isEqualTo(jwtTokenProvider.getPayload(token).get("email"));
  }

  @DisplayName("주어진 이메일로 가입한 멤버가 없으면 예외를 발생한다.")
  @Test
  void findMemberByInvalidEmail() {
    // Given
    final String email = "kelly11@example.com";
    final LoginRequest loginRequest = new LoginRequest(email, "password123");
    // when & than
    assertThatThrownBy(
        () -> authService.login(loginRequest))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("주어진 이메일로 가입한 멤버가 없습니다. (email : " + email + ")");
  }
}
