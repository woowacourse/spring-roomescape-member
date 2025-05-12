package roomescape.member.application;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.member.domain.Member;
import roomescape.member.domain.Role;
import roomescape.member.dto.MemberResponse;
import roomescape.member.infrastructure.MemberRepository;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberService memberService = new MemberService(memberRepository);

    @Test
    @DisplayName("회원 ID로 회원을 조회할 수 있다")
    void test1() {
        // given
        Member expectedMember = new Member(1L, "미미", "email", "password", Role.MEMBER);
        when(memberRepository.findById(1L)).thenReturn(Optional.of(expectedMember));

        // when
        Member result = memberService.findById(1L);

        // then
        assertThat(result).isEqualTo(expectedMember);
    }

    @Test
    @DisplayName("존재하지 않는 회원 ID로 조회하면 예외가 발생한다")
    void test2() {
        // given
        Long notExistedId = 999L;
        when(memberRepository.findById(notExistedId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.findById(notExistedId))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    @DisplayName("전체 회원 목록을 조회할 수 있다")
    void test3() {
        // given
        Member mimi = new Member(1L, "미미", "mimi@email.com", "pw1", Role.MEMBER);
        Member norang = new Member(2L, "노랑", "coco@email.com", "pw2", Role.ADMIN);
        List<Member> members = List.of(mimi, norang);

        when(memberRepository.findAll()).thenReturn(members);

        // when
        List<MemberResponse> result = memberService.findAll();

        // then
        List<MemberResponse> expected = members.stream()
                .map(MemberResponse::from)
                .toList();

        assertThat(result).isEqualTo(expected);
    }
}
