package roomescape.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.domain.repository.MemberRepository;
import roomescape.member.dto.LoginCheckResponse;
import roomescape.member.dto.LoginRequest;
import roomescape.member.util.FakeTokenProvider;
import roomescape.reservation.dao.FakeMemberDao;

class MemberServiceTest {
    MemberRepository memberRepository;
    MemberService memberService;
    Member member;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberDao();
        memberService = new MemberService(memberRepository, new FakeTokenProvider());
        member = memberRepository.save(new Member("test", "test@email.com", "password", Role.MEMBER));
    }

    @DisplayName("로그인에 성공한다.")
    @Test
    void login() {
        // given & when
        String token = memberService.checkLogin(new LoginRequest(member.getEmail(), member.getPassword()));

        //then
        assertThat(token).isEqualTo(member.getId().toString());
    }

    @DisplayName("이메일 혹은 비밀번호가 일치하지 않을 때, 로그인 시 예외가 발생한다.")
    @Test
    void loginNotMatch() {
        // given & when & then
        assertThatThrownBy(() ->
                memberService.checkLogin(new LoginRequest("testt@email.com", "password")))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("토큰을 통해 유저 찾기에 성공한다.")
    @Test
    void findMemberByToken() {
        // given
        String token = memberService.checkLogin(new LoginRequest(member.getEmail(), member.getPassword()));

        // when
        LoginCheckResponse loginCheckResponse = memberService.findMemberNameByToken(token);

        //then
        assertThat(loginCheckResponse.name()).isEqualTo(member.getName());
    }

    @DisplayName("모든 멤버를 조회할 수 있다.")
    @Test
    void findAllMember() {
        //given & when
        List<Member> memberResponses = memberRepository.findAll();

        //then
        assertAll(
                () -> assertThat(memberResponses).hasSize(1),
                () -> assertThat(memberResponses.get(0)).isEqualTo(member)
        );
    }


}
