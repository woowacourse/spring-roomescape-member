package roomescape.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dto.member.LoginMemberResponse;
import roomescape.dto.member.LoginRequest;
import roomescape.dto.member.TokenResponse;
import roomescape.exception.InvalidAuthorizationException;
import roomescape.fixture.FakeMemberRepositoryFixture;
import roomescape.repository.FakeTokenProvider;
import roomescape.repository.MemberRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("사용자 로그인")
class LoginServiceTest {

    private final MemberRepository memberRepository = FakeMemberRepositoryFixture.create();
    private final LoginService loginService = new LoginService(memberRepository, new FakeTokenProvider());

    @DisplayName("올바른 사용자 정보를 전달하면 로그인 토큰을 생성한다")
    @Test
    void createTokenTest() {
        // given
        LoginRequest request = new LoginRequest("wooteco7", "admin@gmail.com");

        // when
        TokenResponse token = loginService.createToken(request);
        String expected = "admin@gmail.com";

        // then
        assertThat(token.accessToken()).isEqualTo(expected);
    }

    @DisplayName("토큰 정보로 사용자를 추출할 수 있다")
    @Test
    void findMemberTest() {
        // given
        String token = "admin@gmail.com";

        // when
        LoginMemberResponse response = loginService.findMemberByToken(token);

        // then
        assertAll(
                () -> assertThat(response.id()).isEqualTo(1L),
                () -> assertThat(response.name()).isEqualTo("어드민")
        );
    }

    @DisplayName("사용자 추출 시 토큰 정보가 잘못되면 예외가 발생한다")
    @Test
    void findMemberExceptionTest() {
        // given
        String token = "invalid";

        // when & then
        assertThatThrownBy(() -> loginService.findMemberByToken(token)).isInstanceOf(InvalidAuthorizationException.class);
    }

    @DisplayName("토큰 생성 시 사용자 정보가 잘못되면 예외가 발생한다")
    @Test
    void createTokenExceptionTest() {
        // given
        LoginRequest request = new LoginRequest("", "admin@gmail.com");

        // when & then
        assertThatThrownBy(() -> loginService.createToken(request)).isInstanceOf(InvalidAuthorizationException.class);
    }
}
