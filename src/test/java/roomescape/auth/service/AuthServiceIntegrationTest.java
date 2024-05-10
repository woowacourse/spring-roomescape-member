package roomescape.auth.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.dto.LoginRequest;
import roomescape.globar.infra.JwtTokenProvider;

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
    String email = "user@mail.com";
    LoginRequest loginRequest = new LoginRequest(email, "1234qwer");
    // When
    String token = authService.createUser(loginRequest);
    // Then
    Assertions.assertThat(email).isEqualTo(jwtTokenProvider.getPayload(token).get("email"));
  }
}
