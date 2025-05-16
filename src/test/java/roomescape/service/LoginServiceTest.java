package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.config.JwtTokenProvider;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.dto.request.LoginRequest;
import roomescape.exception.AuthenticationException;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LoginServiceTest {

    @Mock
    private MemberDao memberDao;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private LoginService loginService;


    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = new Member(1L, "두리", "duri@a.com", "1234", Role.USER);
        loginService = new LoginService(memberDao, jwtTokenProvider);
    }

    @Test
    @DisplayName("로그인 성공 시 JWT 토큰을 반환한다")
    void login() {
        LoginRequest loginRequest = new LoginRequest("duri@a.com", "1234");

        given(memberDao.findByEmail(loginRequest.email())).willReturn(Optional.of(testMember));

        String token = loginService.login(loginRequest);

        Long memberId = jwtTokenProvider.getMemberIdFromToken(token);

        assertThat(memberId).isEqualTo(testMember.getId());
    }

    @Test
    @DisplayName("이메일이 존재하지 않을시 예외를 던진다")
    void login_no_email() {
        LoginRequest loginRequest = new LoginRequest("no@email.com", "1234");
        given(memberDao.findByEmail(loginRequest.email())).willReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.login(loginRequest))
            .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("비밀번호가 올바르지 않으면 예외를 던진다")
    void login_wrong_password() {
        LoginRequest loginRequest = new LoginRequest("duri@a.com", "11");
        given(memberDao.findByEmail(loginRequest.email())).willReturn(Optional.of(testMember));

        assertThatThrownBy(() -> loginService.login(loginRequest))
            .isInstanceOf(AuthenticationException.class);
    }

    @Test
    @DisplayName("유효한 토큰으로 멤버 정보를 조회한다")
    void get_member_by_token() {
        LoginRequest loginRequest = new LoginRequest("duri@a.com", "1234");

        given(memberDao.findByEmail(loginRequest.email())).willReturn(Optional.of(testMember));
        given(memberDao.findById(testMember.getId())).willReturn(Optional.of(testMember));

        String token = loginService.login(loginRequest);

        assertThat(loginService.getLoginMemberByToken(token).getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("토큰의 맴버 id가 존재하지 않을 시 예외를 던진다")
    void get_login_member_with_no_member_id() {
        Long noMemberId = 100L;
        Member unSavedMember = new Member(
            100L,
            "저장안됨",
            "a@a.com",
            "1234",
            Role.USER
        );
        String tokenWithNoMemberId = jwtTokenProvider.get(unSavedMember);

        given(memberDao.findById(noMemberId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> loginService.getLoginMemberByToken(tokenWithNoMemberId))
            .isInstanceOf(AuthenticationException.class);
    }
}
