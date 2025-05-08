package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.domain.Member;
import roomescape.domain.repository.MemberRepository;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.response.AuthenticatedUserResponse;
import roomescape.exception.LoginFailedException;
import roomescape.fake.FakeMemberRepository;
import roomescape.infrastructure.JwtTokenProvider;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    private AuthService authService;
    private MemberRepository memberRepository;

    @Mock
    private JwtTokenProvider tokenProvider;

    @BeforeEach
    void setUp() {
        this.memberRepository = new FakeMemberRepository();
        this.authService = new AuthService(tokenProvider, memberRepository);
    }

    @Test
    void 이메일과_비밀번호로_토큰을_생성한다() {
        // given
        Member member = new Member(null, "name1", "email1@domain.com", "password1");
        memberRepository.save(member);
        given(tokenProvider.createToken(any())).willReturn(any());
        LoginRequest request = new LoginRequest("email1@domain.com", "password1");
        // when & then
        assertThatCode(() -> authService.createToken(request)).doesNotThrowAnyException();
    }

    @Test
    void 존재하지_않는_이메일인_경우_예외가_발생한다() {
        // given
        LoginRequest request = new LoginRequest("email1@domain.com", "password1");
        // when & then
        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void 잘못된_비밀번호인_경우_예외가_발생한다() {
        // given
        Member member = new Member(null, "name1", "email1@domain.com", "password1");
        memberRepository.save(member);
        LoginRequest request = new LoginRequest("email1@domain.com", "password2");
        // when & then
        assertThatThrownBy(() -> authService.createToken(request))
                .isInstanceOf(LoginFailedException.class);
    }

    @Test
    void 토큰을_분해해서_사용자_정보를_조회한다() {
        // given
        Member member = new Member(null, "name1", "email1@domain.com", "password1");
        memberRepository.save(member);
        // when
        AuthenticatedUserResponse token = authService.getAuthenticatedUserFromToken(1L);
        // then
        assertThat(token.name()).isEqualTo("name1");
    }
}