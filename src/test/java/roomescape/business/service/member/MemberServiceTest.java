package roomescape.business.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.domain.member.SignUpMember;
import roomescape.persistence.MemberRepository;
import roomescape.persistence.fakerepository.FakeMemberRepository;

class MemberServiceTest {

    private MemberService memberService;
    private MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @DisplayName("가입한 사용자 목록을 조회한다.")
    @Test
    void getMembers() {
        // given
        String email = "test@email.com";
        String password = "password";
        String name = "test";
        memberRepository.save(new SignUpMember(name, email, password));

        // when
        var members = memberService.getMembers();

        // then
        assertAll(
                () -> assertThat(members).isNotEmpty(),
                () -> assertThat(members.get(0).name()).isEqualTo(name)
        );
    }
}
