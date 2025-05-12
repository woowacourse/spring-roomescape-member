package roomescape.member.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.fake.MemberFakeRepository;
import roomescape.member.dto.MemberResponse;
import roomescape.member.dto.SignUpRequest;
import roomescape.member.dto.SignUpResponse;
import roomescape.member.entity.Member;
import roomescape.member.entity.Role;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberServiceTest {

    private final MemberFakeRepository memberRepository = new MemberFakeRepository();
    private final MemberService memberService = new MemberService(memberRepository);

    @Test
    @DisplayName("유저를 등록한다.")
    void registerUser() {
        SignUpRequest request = new SignUpRequest("abcde@email.com", "1234", "테스트");

        SignUpResponse signUpResponse = memberService.registerUser(request);

        assertThat(signUpResponse.getEmail()).isEqualTo(request.getEmail());
        assertThat(signUpResponse.getRole()).isEqualTo(Role.USER);
        assertThat(signUpResponse.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("유저 id로 특정 유저 정보를 찾는다.")
    void findById() {
        Member saved = memberRepository.save(Member.withoutId("abcd3@email.com", "pw", "회원3", Role.USER));

        Member member = memberService.findById(saved.getId());

        assertThat(member.getEmail()).isEqualTo("abcd3@email.com");
        assertThat(member.getName()).isEqualTo("회원3");
    }

    @Test
    @DisplayName("모든 유저 정보를 찾는다.")
    void findAll() {
        List<MemberResponse> memberResponses = memberService.findAll();

        assertThat(memberResponses.size()).isEqualTo(2);
        assertThat(memberResponses.getFirst().name()).isEqualTo("회원1");
    }
}
