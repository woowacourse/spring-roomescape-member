package roomescape.auth.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.auth.domain.JwtTokenProvider;
import roomescape.auth.domain.Role;
import roomescape.auth.dto.request.TokenRequest;
import roomescape.auth.exception.AuthorizationException;
import roomescape.member.domain.Member;
import roomescape.member.exception.MemberNotFoundException;
import roomescape.member.service.MemberService;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private JwtTokenProvider tokenProvider;

    @Mock
    private MemberService memberService;

    @InjectMocks
    private AuthService authService;

    @Test
    void createToken_shouldCreateToken() {
        // given
        String email = "test@example.com";
        String password = "securePassword";
        Role role = Role.MEMBER;
        Member member = Member.of(1L, "Danny", email, password, role);
        TokenRequest request = new TokenRequest(email, password);

        Mockito.when(memberService.getMember(email, password)).thenReturn(member);
        Mockito.when(tokenProvider.createToken(member.getId().toString())).thenReturn("fake-jwt-token");

        // when
        String token = authService.createToken(request);

        // then
        assertEquals("fake-jwt-token", token);
    }

    @Test
    void createToken_shouldThrowException_IfInvalidLogin() {
        // given
        String email = "wrong@example.com";
        String password = "wrongPassword";
        TokenRequest request = new TokenRequest(email, password);

        Mockito.when(memberService.getMember(email, password)).thenThrow(MemberNotFoundException.class);

        // when & then
        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(AuthorizationException.class);
    }
}
