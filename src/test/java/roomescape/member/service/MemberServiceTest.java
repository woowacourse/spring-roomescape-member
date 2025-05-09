package roomescape.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.MemberRepository;
import roomescape.member.dto.SignupRequest;
import roomescape.member.dto.SignupResponse;

class MemberServiceTest {

    private MemberRepository memberRepository = new FakeMemberRepository(new ArrayList<>());
    private MemberService memberService = new MemberService(memberRepository);

    @Test
    @DisplayName("이미 가입된 이메일에 대한 예외 테스트")
    void createMember_exception() {
        // given
        Member member = Member.createWithoutId("a", "a@naver.com", "a");
        memberRepository.save(member);

        SignupRequest signupRequest = new SignupRequest("a@naver.com", "a", "a");
        // when & then
        assertThatThrownBy(() -> memberService.createMember(signupRequest))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("정상 가입 테스트")
    void createMember_test() {
        // given
        SignupRequest signupRequest = new SignupRequest("a@naver.com", "a", "a");
        SignupResponse expected = new SignupResponse(1L, "a", "a@naver.com", "a");
        // when
        SignupResponse response = memberService.createMember(signupRequest);
        // then
        assertThat(response).isEqualTo(expected);
    }
}
