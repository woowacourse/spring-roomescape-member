package roomescape.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.LoginRequest;
import roomescape.member.util.FakeTokenProvider;
import roomescape.reservation.dao.FakeMemberDao;

class MemberServiceTest {
    MemberRepository memberRepository;
    MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberDao();
        memberService = new MemberService(memberRepository, new FakeTokenProvider());
    }

    @DisplayName("로그인에 성공한다.")
    @Test
    void login() {
        // given
        String email = "test@email.com";
        String password = "password";
        memberRepository.save(new Member("test", email, password));

        // when
        String token = memberService.checkLogin(new LoginRequest(email, password));

        //then
        assertThat(token).isEqualTo(email);
    }

    @DisplayName("이메일 혹은 비밀번호가 일치하지 않을 때, 로그인 시 예외가 발생한다.")
    @Test
    void loginNotMatch() {
        // given & when & then
        assertThatThrownBy(() ->
                memberService.checkLogin(new LoginRequest("test@email.com", "password")))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
