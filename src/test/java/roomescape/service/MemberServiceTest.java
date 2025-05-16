package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static roomescape.constant.TestData.MEMBER_COUNT;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import roomescape.DataBasedTest;
import roomescape.auth.JwtProvider;
import roomescape.domain.Member;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.SignupRequest;
import roomescape.dto.response.LoginCheckResponse;
import roomescape.dto.response.MemberResponse;
import roomescape.repository.MemberRepository;

class MemberServiceTest extends DataBasedTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Test
    void login() {
        // given
        Member member = memberRepository.save(new Member("name", "email@example.com", "password"));

        // when
        String token = memberService.login(new LoginRequest("email@example.com", "password"));
        Long memberId = Long.valueOf(jwtProvider.getPayload(token));

        // then
        assertThat(memberId).isEqualTo(member.id());
    }

    @Test
    void loginCheck() {
        // given
        Member member = memberRepository.save(new Member("name", "email@example.com", "password"));

        // when
        LoginCheckResponse response = memberService.loginCheck(member);

        // then
        assertThat(response.name()).isEqualTo(member.name());
    }

    @Test
    void signup() {
        // given
        SignupRequest request = new SignupRequest( "email@example.com", "name", "password");

        // when
        memberService.signup(request);

        // then
        assertThat(memberRepository.findByEmail("email@example.com")).isPresent();
    }

    @Test
    void getAll() {
        // when
        List<MemberResponse> members = memberService.getAll();

        // then
        assertThat(members).hasSize(MEMBER_COUNT);
    }
}
