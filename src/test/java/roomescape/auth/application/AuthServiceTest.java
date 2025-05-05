package roomescape.auth.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static roomescape.auth.exception.AuthErrorCode.INVALID_PASSWORD;
import static roomescape.auth.exception.AuthErrorCode.INVALID_TOKEN;
import static roomescape.auth.exception.AuthErrorCode.MEMBER_NOT_FOUND;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.auth.domain.Member;
import roomescape.auth.domain.MemberRepository;
import roomescape.auth.dto.TokenRequest;
import roomescape.auth.dto.TokenResponse;
import roomescape.auth.exception.AuthorizationException;
import roomescape.auth.infrastructure.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    JwtTokenProvider jwtTokenProvider;

    @Mock
    MemberRepository memberRepository;

    @InjectMocks
    AuthService authService;

    @Test
    @DisplayName("이메일과 비밀번호로 토큰을 생성할 수 있다")
    void createToken_success() {
        TokenRequest request = new TokenRequest("email@test.com", "1234");
        Member member = new Member("email@test.com", "1234", "멍구");

        given(memberRepository.findByEmail("email@test.com")).willReturn(Optional.of(member));
        given(jwtTokenProvider.createToken("email@test.com")).willReturn("token-value");

        TokenResponse response = authService.createToken(request);

        assertThat(response.accessToken()).isEqualTo("token-value");
    }

    @Test
    @DisplayName("유효한 토큰으로 사용자를 조회할 수 있다")
    void findMemberByToken_success() {
        String token = "valid-token";
        String email = "email@test.com";
        Member member = new Member( email, "pass", "멍구");

        given(jwtTokenProvider.validateToken(token)).willReturn(true);
        given(jwtTokenProvider.getPayload(token)).willReturn(email);
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));

        Member result = authService.findMemberByToken(token);

        assertThat(result.getName()).isEqualTo("멍구");
    }

    @Test
    @DisplayName("비밀번호가 일치하지 않으면 예외가 발생한다")
    void createToken_wrongPassword() {
        TokenRequest request = new TokenRequest("email@test.com", "wrong");
        Member member = new Member("email@test.com", "password", "멍구");

        given(memberRepository.findByEmail("email@test.com")).willReturn(Optional.of(member));

        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage(INVALID_PASSWORD.getMessage());
    }

    @Test
    @DisplayName("유효하지 않은 토큰은 예외를 발생시킨다")
    void findMemberByToken_invalidToken() {
        String token = "invalid-token";

        given(jwtTokenProvider.validateToken(token)).willReturn(false);

        assertThatThrownBy(() -> authService.findMemberByToken(token))
                .isInstanceOf(AuthorizationException.class)
                .hasMessage(INVALID_TOKEN.getMessage());
    }

    @DisplayName("존재하지 않는 이메일로 로그인 시도하면 예외를 발생시킨다.")
    @Test
    void findMemberByToken_emailNotFound() {
        // given
        TokenRequest tokenRequest = new TokenRequest("notfound@email.com", "password");
        given(memberRepository.findByEmail(tokenRequest.email())).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> authService.createToken(tokenRequest))
                .isInstanceOf(AuthorizationException.class)
                .hasMessageContaining(MEMBER_NOT_FOUND.getMessage());
    }
}
