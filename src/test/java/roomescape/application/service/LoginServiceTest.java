package roomescape.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import roomescape.application.dto.LoginRequest;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;
import roomescape.domain.Role;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private MemberDao memberDao;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(loginService, "secretKey",
                "HabQK/WQPor/4qDcxy4Nw8gzdixE/sIEsieNlVmzR554kzF5EyMCL6de3TSsW4AsVQ+87rxaymg9HbYSopAngw==");
    }

    @Test
    @DisplayName("일치하는 회원인 경우 토큰을 반환한다")
    void createTokenForValidMember() {
        // given
        String email = "test@email.com";
        LoginRequest request = new LoginRequest("password", email);
        Member member = new Member(1L, "name", email, "password", Role.ADMIN);

        doReturn(Optional.of(member)).when(memberDao)
                .findByEmail(email);

        // when
        String token = loginService.createTokenForAuthenticatedMember(request);

        // then
        assertThat(token)
                .isNotNull()
                .isNotBlank()
                .hasSizeGreaterThan(10);

        // verify
        verify(memberDao, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("가입되지 않은 이메일로 로그인시 예외가 발생한다")
    void createTokenWithNonExistingMemberEmail() {
        // given
        String email = "test@email.com";
        LoginRequest request = new LoginRequest("password", email);
        Optional<Member> member = Optional.empty();

        doReturn(member).when(memberDao)
                .findByEmail(email);

        // when && then
        assertThatThrownBy(() -> loginService.createTokenForAuthenticatedMember(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로그인을 실패했습니다. 정보를 다시 확인해 주세요.");

        // verify
        verify(memberDao, times(1)).findByEmail(email);
    }

    @Test
    @DisplayName("로그인시 비밀번호가 다른 경우 예외가 발생한다")
    void createTokenWithWrongPassword() {
        // given
        String email = "test@email.com";
        LoginRequest request = new LoginRequest("otherPassword", email);
        Member member = new Member(1L, "name", email, "password", Role.ADMIN);

        doReturn(Optional.of(member)).when(memberDao)
                .findByEmail(email);

        // when && then
        assertThatThrownBy(() -> loginService.createTokenForAuthenticatedMember(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로그인을 실패했습니다. 정보를 다시 확인해 주세요.");

        // verify
        verify(memberDao, times(1)).findByEmail(email);
    }
}
