package roomescape.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.auth.domain.Member;
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
    String email = "kelly@example.com";
    LoginRequest loginRequest = new LoginRequest(email, "password123");
    // When
    String token = authService.login(loginRequest);
    // Then
    assertThat(email).isEqualTo(jwtTokenProvider.getPayload(token).get("email"));
  }

  @DisplayName("이메일로 가입한 회원정보를 조회한다.")
  @Test
  void findMemberByEmail() {
    // given
    String email = "kelly@example.com";
    // when
    Member member = authService.findMemberByEmail(email);
    //then
    assertThat(member.getEmail()).isEqualTo(email);
  }

  @DisplayName("주어진 이메일로 가입한 멤버가 없으면 예외를 발생한다.")
  @Test
  void findMemberByInvalidEmail() {
    // given
    String email = "kellyyy@example.com";
    // when & than
    assertThatThrownBy(
        () -> authService.findMemberByEmail(email))
        .isInstanceOf(NoSuchElementException.class)
        .hasMessage("주어진 이메일로 가입한 멤버가 없습니다. (email : " + email + ")");
  }

  @DisplayName("주어진 토큰으로 어드민 권한을 확인한다.")
  @Test
  void checkAdminPermission() {
    // given
    String token = jwtTokenProvider.createToken("neo@example.com", "네오");
    // when 
    boolean isAdmin = authService.checkAdminPermission(token);
    //then
    assertThat(isAdmin).isTrue();
  }
}
