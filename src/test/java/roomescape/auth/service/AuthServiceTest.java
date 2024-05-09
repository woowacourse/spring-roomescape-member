package roomescape.auth.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.dto.LoginRequest;
import roomescape.auth.token.AuthenticationToken;
import roomescape.member.model.Member;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql(value = {"/schema.sql", "/data.sql"}, executionPhase = BEFORE_TEST_METHOD)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @DisplayName("유효한 로그인 정보가 입력되면 인증 토큰을 반환한다.")
    @Test
    void loginTest() {
        // Given
        final LoginRequest loginRequest = new LoginRequest("user@mail.com", "userPw1234!");

        // When
        final AuthenticationToken authenticationToken = authService.login(loginRequest);

        // Then
        assertThat(authenticationToken.validate()).isTrue();
    }

    @DisplayName("존재하지 않는 회원 이메일 정보를 입력하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenUnknownEmail() {
        // Given
        final LoginRequest loginRequest = new LoginRequest("who@mail.com", "userPw1234!");

        // When & Then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 이메일 정보와 일치하는 회원 정보가 없습니다.");
    }

    @DisplayName("잘못된 비밀번호를 입력하면 예외가 발생한다.")
    @Test
    void throwExceptionWhenInvalidPassword() {
        // Given
        final LoginRequest loginRequest = new LoginRequest("user@mail.com", "userPw1234!111");

        // When & Then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("일치하지 않는 비밀번호입니다.");
    }

    @DisplayName("AccessToken 입력하면 인증된 사용자 정보를 반환한다.")
    @Test
    void findAuthenticatedMemberTest() {
        // Given
        final String accessToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbkBtYWlsLmNvbSIsImlhdCI6MTcxNTI0MDM1NCwiZX" +
                "hwIjoxNzE1ODQ1MTU0LCJyb2xlIjoiQURNSU4ifQ.ouMYZ8bzNDauM7xzUDj9SgaJU_8" +
                "OT4fbEoBZ2oRteJXU8Cr2yyymXqyLToxzc39PqXwhrWxflnjMgMokqi610g";

        // When
        final Member member = authService.findAuthenticatedMember(accessToken);

        // Then
        assertThat(member.getEmail().value()).isEqualTo("admin@mail.com");
    }
}
