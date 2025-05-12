package roomescape.business.service;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.business.Member;
import roomescape.business.MemberRole;
import roomescape.persistence.FakeMemberRepository;
import roomescape.persistence.MemberRepository;
import roomescape.presentation.dto.response.MemberResponseDto;

class MemberServiceTest {

    private MemberRepository memberRepository;
    private MemberService memberService;

    @BeforeEach
    void setUp() {
        memberRepository = new FakeMemberRepository();
        memberService = new MemberService(memberRepository);
    }

    @Test
    @DisplayName("사용자 목록을 불러온다.")
    void test() {
        // given
        Member member1 = new Member("user1", "user1@woowa.com", "1234", MemberRole.USER);
        Member member2 = new Member("user2", "user2@woowa.com", "1234", MemberRole.USER);
        Long member1Id = memberRepository.add(member1);
        Long member2Id = memberRepository.add(member2);

        // when
        List<MemberResponseDto> readMembers = memberService.readMemberAll();

        // then
        Assertions.assertThat(readMembers).hasSize(2);
        Assertions.assertThat(readMembers.get(0))
                .isEqualTo(new MemberResponseDto(member1Id, "user1", "user1@woowa.com"));
        Assertions.assertThat(readMembers.get(1))
                .isEqualTo(new MemberResponseDto(member2Id, "user2", "user2@woowa.com"));
    }
}