package roomescape.business.service.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.config.AccessToken;
import roomescape.config.LoginMember;
import roomescape.exception.MemberException;
import roomescape.exception.UnAuthorizedException;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.fakerepository.FakeMemberRepository;
import roomescape.presentation.auth.dto.LoginRequestDto;
import roomescape.presentation.auth.dto.MemberRequestDto;

class AuthServiceTest {

    private AuthService authService;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        authService = new AuthService(memberRepository);
    }

    @DisplayName("회원 가입을 한다.")
    @Test
    void registerMember() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, name);

        // when
        Long memberId = authService.registerMember(memberRequestDto);

        // then
        assertAll(
                () -> assertThat(memberRepository.findById(memberId))
                        .isPresent(),
                () -> assertThat(memberRepository.findById(memberId).get().getName())
                        .isEqualTo(name),
                () -> assertThat(memberRepository.findById(memberId).get().getEmail())
                        .isEqualTo(email)
        );
    }

    @DisplayName("회원 가입 시 중복된 이메일로 가입을 시도하면 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenDuplicateEmail() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, name);
        authService.registerMember(memberRequestDto);

        // when
        // then
        assertThatCode(() -> authService.registerMember(memberRequestDto))
                .isInstanceOf(MemberException.class)
                .hasMessage("이미 존재하는 이메일입니다.");
    }

    @DisplayName("로그인 시 존재하지 않는 이메일로 로그인 시도하면 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenInvalidEmailOrPassword() {
        // given
        String email = "test@email.com";
        String password = "password";
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);

        // when
        // then
        assertThatCode(() -> authService.login(loginRequestDto))
                .isInstanceOf(MemberException.class)
                .hasMessage("이메일 또는 비밀번호가 잘못되었습니다.");
    }

    @DisplayName("로그인 시 일치하지 않는 비밀번호로 로그인 시도하면 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenInvalidPassword() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, name);
        authService.registerMember(memberRequestDto);
        String invalidPassword = "invalidPassword";
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, invalidPassword);

        // when
        // then
        assertThatCode(() -> authService.login(loginRequestDto))
                .isInstanceOf(MemberException.class)
                .hasMessage("이메일 또는 비밀번호가 잘못되었습니다.");
    }

    @DisplayName("성공적인 로그인 시 AccessToken을 반환한다.")
    @Test
    void login() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        MemberRequestDto memberRequestDto = new MemberRequestDto(email, password, name);
        authService.registerMember(memberRequestDto);
        LoginRequestDto loginRequestDto = new LoginRequestDto(email, password);

        // when
        AccessToken accessToken = authService.login(loginRequestDto);

        // then
        assertAll(
                () -> assertThat(accessToken).isNotNull(),
                () -> assertThat(accessToken.extractMemberId()).isEqualTo(1L)
        );
    }

    @DisplayName("로그인하지 않은 경우 예외가 발생한다.")
    @Test
    void shouldThrowException_WhenLoginMemberIsNull() {
        // given
        LoginMember loginMember = null;

        // when
        // then
        assertThatCode(() -> authService.checkLogin(loginMember))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessage("로그인이 필요합니다.");
    }
}
