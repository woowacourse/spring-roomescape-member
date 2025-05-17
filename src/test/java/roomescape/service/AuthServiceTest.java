package roomescape.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.dto.LoginRequest;
import roomescape.exception.exception.InvalidLoginInfoException;
import roomescape.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    MemberRepository memberRepository;
    @InjectMocks
    AuthService authService;

    @Test
    void loginWithNotFoundMember() {
        // given
        String notExistEmail = "wilson@gmail.com";
        LoginRequest loginRequest = new LoginRequest(notExistEmail, "1234!@#$");
        when(memberRepository.findByEmail(notExistEmail)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(InvalidLoginInfoException.class)
                .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

    @Test
    void loginFailedByIncorrectPassword() {
        // given
        String email = "wilson@gmail.com";
        when(memberRepository.findByEmail(email))
                .thenReturn(Optional.of(new Member("윌슨", "wilson@gmail.com", "1234!@#$", MemberRole.USER)));
        LoginRequest loginRequest = new LoginRequest(email, "1234");

        // when & then
        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(InvalidLoginInfoException.class)
                .hasMessage("아이디 또는 비밀번호가 올바르지 않습니다.");
    }

}
