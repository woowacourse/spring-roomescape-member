package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;

@ExtendWith(MockitoExtension.class)
public class MemberLoginServiceTest {

    @Mock
    private MemberService memberService;

    @Mock
    private AuthService authService;

    @InjectMocks
    private MemberLoginService memberLoginService;

    @Test
    void findByTokenTest() {

        // given
        final Member member = new Member("체체", "cheche903@naver.com", "password1234", MemberRole.USER);
        final String token = "token";
        when(authService.getPayload(token)).thenReturn("cheche903@naver.com");
        when(memberService.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        // when
        assertThat(memberLoginService.findByToken(token).name()).isEqualTo("체체");
    }
}
