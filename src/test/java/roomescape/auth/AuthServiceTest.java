package roomescape.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.auth.dto.LoginRequest;
import roomescape.exception.custom.reason.auth.AuthNotExistsEmailException;
import roomescape.exception.custom.reason.auth.AuthNotValidPasswordException;
import roomescape.member.FakeMemberRepository;
import roomescape.member.Member;

public class AuthServiceTest {

    private final AuthService authService;
    private final FakeMemberRepository fakeMemberRepository;
    private final StubJwtProvider stubJwtProvider;

    public AuthServiceTest(){
        fakeMemberRepository = new FakeMemberRepository();
        stubJwtProvider = new StubJwtProvider();
        authService = new AuthService(fakeMemberRepository, stubJwtProvider);
    }

    @DisplayName("토큰을 발급한다.")
    @Test
    void generateToken() {
        // given
        final String token = "token";
        final LoginRequest request = new LoginRequest("admin@email.com", "pw1234");
        fakeMemberRepository.saveMember(new Member("admin@email.com", "pw1234", "부기"));
        stubJwtProvider.stubToken(token);

        // when
        final String actual = authService.generateToken(request);

        // then
        assertThat(actual).isEqualTo(token);
    }

    @DisplayName("유저 이메일이 존재하지 않는다면, 예외가 발생한다.")
    @Test
    void generateToken1() {
        // given
        final String token = "token";
        final LoginRequest request = new LoginRequest("admin@email.com", "pw1234");
        stubJwtProvider.stubToken(token);

        // when & then
        assertThatThrownBy(() -> {
            authService.generateToken(request);
        }).isInstanceOf(AuthNotExistsEmailException.class);
    }

    @DisplayName("비밀번호가 일치하지 않는다면, 예외가 발생한다.")
    @Test
    void generateToken2() {
        // given
        final String token = "token";
        final LoginRequest request = new LoginRequest("admin@email.com", "not matches password");
        fakeMemberRepository.saveMember(new Member("admin@email.com", "pw1234", "부기"));
        stubJwtProvider.stubToken(token);

        // when & then
        assertThatThrownBy(() -> {
            authService.generateToken(request);
        }).isInstanceOf(AuthNotValidPasswordException.class);
    }


}
