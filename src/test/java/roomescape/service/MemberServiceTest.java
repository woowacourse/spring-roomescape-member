package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.repository.member.MemberRepository;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberService memberService = new MemberService(memberRepository);

    @DisplayName("특정 권한에 해당하는 회원들을 조회할 수 있다")
    @Test
    void testMethodNameHere() {
        // given
        List<Member> expectedMembers = List.of(
                new Member(1L, "회원1", "test1@test.com", "asdf1234!", MemberRole.GENERAL),
                new Member(2L, "회원2", "test2@test.com", "asdf1234!", MemberRole.GENERAL),
                new Member(3L, "회원3", "test3@test.com", "asdf1234!", MemberRole.GENERAL));
        when(memberRepository.findAllByRole(MemberRole.GENERAL)).thenReturn(expectedMembers);

        // when
        List<Member> members = memberService.getAllByRole(MemberRole.GENERAL);

        // then
        assertThat(members).hasSize(expectedMembers.size());
    }
}
