package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import roomescape.auth.TokenProvider;
import roomescape.dto.MemberResponse;
import roomescape.dto.TokenRequest;
import roomescape.dto.TokenResponse;
import roomescape.exception.AuthenticationException;
import roomescape.model.Member;
import roomescape.model.Role;
import roomescape.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql(scripts = {"/reset.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @DisplayName("토큰을 올바르게 생성")
    @Test
    void createToken() {
        final String email = "111@aaa.com";
        final String password = "abc1234";
        final Member member = memberRepository.save(new Member("감자", Role.USER, email, password));
        final TokenRequest tokenRequest = new TokenRequest(email, password);
        final TokenResponse tokenResponse = authService.createToken(tokenRequest);

        assertThat(tokenProvider.getMemberId(tokenResponse.accessToken()))
                .isEqualTo(member.getId());
    }

    @DisplayName("가입되지 않은 이메일일 경우 예외 발생")
    @Test
    void loginEmailNotFound() {
        final TokenRequest tokenRequest = new TokenRequest("111@aaa.com", "abc1234");
        assertThatThrownBy(() -> authService.createToken(tokenRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("가입되지 않은 이메일입니다.");
    }

    @DisplayName("비밀번호가 일치하지 않을 경우 예외 발생")
    @Test
    void wrongPassword() {
        final String email = "111@aaa.com";
        memberRepository.save(new Member("감자", Role.USER, email, "abc1111"));
        final TokenRequest tokenRequest = new TokenRequest("111@aaa.com", "abc1234");
        assertThatThrownBy(() -> authService.createToken(tokenRequest))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @DisplayName("토큰을 통해 멤버 조회")
    @Test
    void findMemberByToken() {
        final String email = "111@aaa.com";
        final String password = "abc1234";
        final Member member = memberRepository.save(new Member("감자", Role.USER, email, password));
        final TokenResponse tokenResponse = authService.createToken(new TokenRequest(email, password));
        final MemberResponse memberResponse = authService.findMemberByToken(tokenResponse.accessToken());
        assertThat(memberResponse).isEqualTo(new MemberResponse(member));
    }
}
