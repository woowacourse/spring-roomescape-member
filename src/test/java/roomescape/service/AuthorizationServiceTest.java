package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.persistence.MemberRepository;
import roomescape.service.request.MemberLoginRequest;
import roomescape.service.response.Token;
import roomescape.web.exception.AuthenticationException;

@SpringBootTest
@Sql(scripts = "/member_test_data.sql")
class AuthorizationServiceTest {

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("이메일, 비밀번호를 입력받아 토큰을 생성한다.")
    void login() {
        // given
        MemberLoginRequest request = new MemberLoginRequest("user1@gmail.com", "user1");

        // when
        Token token = authorizationService.login(request);

        // then
        assertThat(token.value()).isNotBlank();
    }

    @Test
    @DisplayName("올바르지 않은 이메일, 비밀번호로 로그인을 시도하면 로그인에 실패한다.")
    void inValidLogin() {
        // given
        MemberLoginRequest request = new MemberLoginRequest("user1@gmail.com", "1234");

        // when, then
        assertThatThrownBy(() -> authorizationService.login(request)).isInstanceOf(AuthenticationException.class);
    }
}
