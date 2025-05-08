package roomescape.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.dao.MemberDao;
import roomescape.domain.Member;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private LoginService loginService;

    @Mock
    private MemberDao memberDao;

    @Test
    @DisplayName("일치하는 회원인 경우 토큰을 반환한다")
    void test() {
        // given
        String email = "test@email.com";
        Member member = new Member(1L, "name", email, "password");

        doReturn(member).when(memberDao)
                .findByEmail(email);

        // when
        String token = loginService.createToken(email);

        // then
        assertThat(token)
                .isNotNull()
                .isNotBlank()
                .hasSizeGreaterThan(10);

        // verify
        verify(loginService, times(1)).createToken(email);
    }
}
