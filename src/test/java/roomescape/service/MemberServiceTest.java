package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Member;
import roomescape.domain.MemberRole;
import roomescape.dto.request.MemberCreationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.NotFoundException;
import roomescape.repository.member.MemberRepository;

class MemberServiceTest {

    private final MemberRepository memberRepository = mock(MemberRepository.class);
    private final MemberService memberService = new MemberService(memberRepository);

    @DisplayName("특정 권한에 해당하는 회원들을 조회할 수 있다")
    @Test
    void canGetAllByRole() {
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

    @DisplayName("특정 ID에 해당하는 회원들을 조회할 수 있다")
    @Test
    void canGetById() {
        // given
        Member expectedMember = new Member(1L, "회원1", "test1@test.com", "asdf1234!", MemberRole.GENERAL);
        when(memberRepository.findById(expectedMember.getId())).thenReturn(Optional.of(expectedMember));

        // when
        Member actualMember = memberService.getById(expectedMember.getId());

        // then
        assertThat(actualMember).isEqualTo(expectedMember);
    }

    @DisplayName("특정 ID에 해당하는 회원을 조회할 때, 회원이 존재하지 않으면 예외를 발생시킨다")
    @Test
    void cannotGetById() {
        // given
        Member expectedMember = new Member(1L, "회원1", "test1@test.com", "asdf1234!", MemberRole.GENERAL);
        when(memberRepository.findById(expectedMember.getId())).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberService.getById(expectedMember.getId()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("[ERROR] 존재하지 않는 회원입니다.");
    }

    @DisplayName("회원을 추가할 수 있다")
    @Test
    void canAddMember() {
        // given
        MemberCreationRequest request = new MemberCreationRequest("test@test.coma", "asdf1234!", "Member1");
        Member expectedMember = Member.createWithoutId(
                request.name(), request.email(), request.password(), MemberRole.GENERAL);
        when(memberRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(memberRepository.add(expectedMember)).thenReturn(1L);

        // when
        long savedId = memberService.addMember(request);

        // then
        assertThat(savedId).isEqualTo(1L);
    }

    @DisplayName("회원을 추가할 때, 중복된 이메일은 허용하지 않는다")
    @Test
    void cannotAddMemberBecauseOfEmailDuplication() {
        // given
        MemberCreationRequest request = new MemberCreationRequest("test@test.coma", "asdf1234!", "Member1");
        Member member = Member.createWithoutId(
                request.name(), request.email(), request.password(), MemberRole.GENERAL);
        when(memberRepository.findByEmail(request.email())).thenReturn(Optional.of(member));

        // when & then
        assertThatThrownBy(() -> memberService.addMember(request))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("[ERROR] 이미 존재하는 계정입니다.");
    }
}
