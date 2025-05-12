package roomescape.business.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.Member;
import roomescape.business.MemberRole;
import roomescape.persistence.FakeMemberRepository;
import roomescape.exception.MemberException;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.dto.request.LoginRequestDto;

class AuthenticationServiceTest {

    private MemberRepository memberRepository;
    private AuthenticationService authenticationService;

    private String email;
    private String password;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        email = "sooyang@woowa.com";
        password = "1234";
        Member member = new Member("sooyang", email, password, MemberRole.USER);
        memberRepository.add(member);
        authenticationService = new AuthenticationService(memberRepository, null);
    }

    @Test
    @DisplayName("존재하는 이메일과 패스워드로 로그인하면 예외가 발생하지 않는다.")
    void login() {
        // given
        LoginRequestDto loginDto = new LoginRequestDto(email, password);

        // when
        // then
        Assertions.assertThatCode(() -> authenticationService.login(loginDto));
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인하면 예외가 발생한다.")
    void loginWithInvalidEmail() {
        // given
        LoginRequestDto loginDto = new LoginRequestDto("no@woowa.com", password);

        // when
        // then
        Assertions.assertThatThrownBy(() -> authenticationService.login(loginDto))
                .isInstanceOf(MemberException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }

    @Test
    @DisplayName("일치하지 않는 비밀번호로 로그인하면 예외가 발생한다")
    void loginWithInvalidPassword() {
        // given
        LoginRequestDto loginDto = new LoginRequestDto(email, "123");

        // when
        // then
        Assertions.assertThatThrownBy(() -> authenticationService.login(loginDto))
                .isInstanceOf(MemberException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}