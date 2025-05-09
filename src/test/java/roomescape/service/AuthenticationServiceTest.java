package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.exception.UnauthorizedException;
import roomescape.repository.member.H2MemberRepository;
import roomescape.repository.member.MemberRepository;
import roomescape.utility.JwtTokenProvider;

class AuthenticationServiceTest {

    private final MemberRepository memberRepository = mock(H2MemberRepository.class);
    private final JwtTokenProvider jwtTokenProvider = new JwtTokenProvider("test_secret_key", 7200);
    private final AuthenticationService authenticationService =
            new AuthenticationService(memberRepository, jwtTokenProvider);

    @DisplayName("로그인을 할 수 있다")
    @Test
    void canLogin() {
        // given
        String email = "test@test.com";
        String password = "test_password";
        Member member = new Member(1L, "이름", email, password, MemberRole.GENERAL);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        // when
        String expectedToken = authenticationService.login(email, password);

        // then
        String actualToken = jwtTokenProvider.makeAccessToken(member.getId(), member.getName(), MemberRole.GENERAL);
        assertThat(expectedToken).isEqualTo(actualToken);
    }

    @DisplayName("로그인할 때, 이메일에 해당하는 회원이 없는 경우 예외를 발생시킨다")
    @Test
    void cannotLoginBecauseOfNotMember() {
        // given
        String email = "test@test.com";
        String password = "test_password";
        when(memberRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authenticationService.login(email, password))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("[ERROR] 유효하지 않은 인증정보입니다.");
    }

    @DisplayName("로그인할 때, 비밀번호가 맞는 않는 경우 예외를 발생시킨다")
    @Test
    void cannotLoginBecauseOfInvalidPassword() {
        // given
        String email = "test@test.com";
        String password = "test_password";
        Member member = new Member(1L, "이름", email, password, MemberRole.GENERAL);
        when(memberRepository.findByEmail(email)).thenReturn(Optional.of(member));

        // when
        assertThatThrownBy(() -> authenticationService.login(email, "wrong_password"))
                .isInstanceOf(UnauthorizedException.class)
                .hasMessage("[ERROR] 유효하지 않은 인증정보입니다.");
    }
}
