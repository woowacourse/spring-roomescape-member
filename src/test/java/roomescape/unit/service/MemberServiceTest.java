package roomescape.unit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.Role;
import roomescape.dto.request.LoginRequest;
import roomescape.dto.request.SignupRequest;
import roomescape.exception.InvalidMemberException;
import roomescape.repository.MemberRepository;
import roomescape.service.MemberService;
import roomescape.unit.repository.FakeMemberRepository;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @BeforeEach
    void setup() {
        memberRepository = new FakeMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @Test
    void 멤버를_추가한다() {
        // given
        SignupRequest request = new SignupRequest("피케이", "pkkk@test.com", "test");

        // when
        Member result = memberService.addMember(request);

        //then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 동일한_이메일을_추가할_수_없다() {
        // given
        String sameEmail = "pkkk@test.com";
        memberRepository.add(new Member("피케이", sameEmail, "test", Role.USER));
        SignupRequest request = new SignupRequest("피케케", sameEmail, "test");

        // when & then
        assertThatThrownBy(() -> memberService.addMember(request))
                .isInstanceOf(InvalidMemberException.class);
    }

    @Test
    void 모든_멤버를_찾는다() {
        // given
        memberRepository.add(new Member("훌라", "hula@test.com", "test", Role.USER));
        memberRepository.add(new Member("피케이", "pkkk@test.com", "test", Role.USER));

        // when
        List<Member> result = memberService.findAll();

        //then
        assertThat(result).hasSize(2);
    }

    @Test
    void 이메일과_패스워드를_대조해_멤버를_찾는다() {
        // given
        LoginRequest request = new LoginRequest("hula@test.com", "test");
        memberRepository.add(new Member("훌라", request.email(), request.password(), Role.USER));

        // when
        Member member = memberService.findByEmailAndPassword(request);

        //then
        assertThat(member.getEmail()).isEqualTo(request.email());
    }

    @Test
    void 멤버_아이디를_통해_찾는다() {
        // given
        Member member = memberRepository.add(new Member("훌라", "hula@test.com", "test", Role.USER));
        long memberId = member.getId();

        // when
        Member result = memberService.getMemberById(memberId);

        //then
        assertThat(result.getId()).isEqualTo(memberId);
    }
}
