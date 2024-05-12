package roomescape.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static roomescape.InitialMemberFixture.MEMBER_4;

import javax.naming.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.login.dto.LoginRequest;
import roomescape.login.service.LoginService;
import roomescape.member.dto.MemberNameResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"/schema.sql", "/initial_test_data.sql"})
class MemberServiceTest {

    @Autowired
    private MemberService memberService;
    @Autowired
    private LoginService loginService;

    @Test
    @DisplayName("유효하지 않은 형식의 토큰으로 로그인 시도 시 예외가 발생한다.")
    void throwExceptionIfInvalidTokenFormat() {
        assertThatThrownBy(() -> memberService.getMemberNameResponseByToken("invalid token"))
                .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("토큰에 대응하는 멤버 정보를 가져온다.")
    void getMemberMember() throws AuthenticationException {
        LoginRequest loginRequest = new LoginRequest(MEMBER_4.getPassword().password(), MEMBER_4.getEmail().email());
        String token = loginService.createLoginToken(loginRequest);

        MemberNameResponse memberNameResponse = memberService.getMemberNameResponseByToken(token);

        assertThat(memberNameResponse.name()).isEqualTo(MEMBER_4.getName().name());
    }
}
