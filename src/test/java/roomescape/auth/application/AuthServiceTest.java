package roomescape.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.domain.Payload;
import roomescape.auth.domain.Token;
import roomescape.auth.infrastructure.Authenticator;
import roomescape.member.application.MemberNotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.LoginRequest;
import roomescape.member.infrastructure.MemberRepository;

class AuthServiceTest {
    private final Authenticator authenticator = mock(Authenticator.class);
    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final AuthService authService = new AuthService(authenticator, memberRepository);

    @Test
    @DisplayName("유효한 로그인 요청으로 토큰을 발급받을 수 있다")
    void test1() {
        // given
        LoginRequest request = new LoginRequest("email", "password");
        Member member = new Member(1L, "미미", "email", "password", Role.MEMBER);
        Token expectedToken = new Token("token");
        Payload payload = Payload.from(member);

        when(memberRepository.findByEmailAndPassword("email", "password")).thenReturn(Optional.of(member));
        when(authenticator.authenticate(payload)).thenReturn(expectedToken);

        // when
        Token token = authService.login(request);

        // then
        assertThat(token).isEqualTo(expectedToken);
    }

    @Test
    @DisplayName("존재하지 않는 회원 정보로 로그인하면 예외가 발생한다")
    void test2() {
        // given
        LoginRequest request = new LoginRequest("email", "wrong-password");

        when(memberRepository.findByEmailAndPassword("email", "wrong-password"))
                .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("유효한 토큰으로 회원을 조회할 수 있다")
    void test3() {
        // given
        Token token = new Token("valid-token");
        Payload payload = new Payload(1L, Role.MEMBER);
        Member expectedMember = new Member(1L, "미미", "email", "password", Role.MEMBER);

        when(authenticator.isInvalidAuthentication(token)).thenReturn(false);
        when(authenticator.getPayload(token)).thenReturn(payload);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(expectedMember));

        // when
        Member result = authService.findMemberByToken(token);

        // then
        assertThat(result).isEqualTo(expectedMember);
    }

    @Test
    @DisplayName("토큰이 유효하지 않으면 회원 조회 시 예외가 발생한다")
    void test4() {
        // given
        Token token = new Token("invalid-token");

        when(authenticator.isInvalidAuthentication(token)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.findMemberByToken(token))
                .isInstanceOf(AuthorizationException.class);
    }

    @Test
    @DisplayName("토큰에서 Payload를 정상적으로 추출할 수 있다")
    void test5() {
        // given
        Token token = new Token("valid-token");
        Payload expectedPayload = new Payload(1L, Role.MEMBER);

        when(authenticator.isInvalidAuthentication(token)).thenReturn(false);
        when(authenticator.getPayload(token)).thenReturn(expectedPayload);

        // when
        Payload result = authService.getPayload(token);

        // then
        assertThat(result).isEqualTo(expectedPayload);
    }

    @Test
    @DisplayName("Payload 추출 시 토큰이 유효하지 않으면 예외가 발생한다")
    void test6() {
        // given
        Token token = new Token("invalid-token");

        when(authenticator.isInvalidAuthentication(token)).thenReturn(true);

        // when & then
        assertThatThrownBy(() -> authService.getPayload(token))
                .isInstanceOf(AuthorizationException.class);
    }
}
