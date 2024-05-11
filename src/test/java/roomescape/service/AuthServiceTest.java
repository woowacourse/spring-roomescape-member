package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

import java.util.NoSuchElementException;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.transaction.annotation.Transactional;
import roomescape.config.TestConfig;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.LoginedMemberResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.security.JwtTokenProvider;

@SpringBootTest(classes = TestConfig.class)
@Transactional
class AuthServiceTest {

    private static final String EMAIL = "auth@gmail.com";
    private static final String PASSWORD = "password";
    private static final String NICKNAME = "nickname";

    @Autowired
    private AuthService authService;

    @Autowired
    private MemberService memberService;

    @SpyBean
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        SignupRequest request = new SignupRequest(EMAIL, PASSWORD, NICKNAME);
        memberService.createMember(request);
    }

    @Test
    @DisplayName("토큰을 생성한다.")
    void createToken() {
        doReturn("created_token").when(jwtTokenProvider).createToken(any());

        LoginRequest request = new LoginRequest(EMAIL, PASSWORD);

        LoginedMemberResponse loginedMemberResponse = authService.createToken(request);
        String token = loginedMemberResponse.token();
        MemberResponse memberResponse = loginedMemberResponse.memberResponse();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(token).isEqualTo("created_token");
            softly.assertThat(memberResponse.email()).isEqualTo(EMAIL);
            softly.assertThat(memberResponse.name()).isEqualTo(NICKNAME);
        });
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않을 경우 토큰을 생성할 수 없다.")
    void createToken_fail_when_password_not_matched() {
        LoginRequest request = new LoginRequest(EMAIL, "wrong_password");

        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }

    @Test
    @DisplayName("이메일이 존재하지 않을 경우 토큰을 생성할 수 없다.")
    void createToken_fail_when_email_not_matched() {
        LoginRequest request = new LoginRequest("not_exist_email", PASSWORD);

        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("해당 이메일의 회원이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("토큰으로 회원 아이디를 가져올 수 있다.")
    void getMemberId() {
        doReturn(1L).when(jwtTokenProvider).getMemberId(any());

        Long memberId = authService.getMemberIdByToken("token");

        assertThat(memberId).isEqualTo(1L);
    }
}
