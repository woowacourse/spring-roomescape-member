package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.dto.LoginRequest;
import roomescape.exception.exception.DataNotFoundException;
import roomescape.exception.exception.InvalidLoginInfoException;
import roomescape.repository.MemberRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
class AuthServiceTest {

    @Autowired
    private AuthService authService;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void loginWithNotFoundMember() {
        // given
        Member member = new Member("제프리", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        memberRepository.save(member);
        LoginRequest loginRequest = new LoginRequest("wilson@gmail.com", "1234!@#$");

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(DataNotFoundException.class)
                .hasMessage("[ERROR] 이메일에 해당하는 회원을 찾을 수 없습니다.");
    }

    @Test
    void loginFailedByIncorrectPassword() {
        // given
        Member member = new Member("제프리", "jeffrey@gmail.com", "1234!@#$", MemberRole.USER);
        memberRepository.save(member);
        LoginRequest loginRequest = new LoginRequest("jeffrey@gmail.com", "1234!@");

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(InvalidLoginInfoException.class)
                .hasMessage("[ERROR] 비밀번호가 일치하지 않습니다.");
    }

}
