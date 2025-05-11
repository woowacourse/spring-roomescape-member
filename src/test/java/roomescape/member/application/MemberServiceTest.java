package roomescape.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static roomescape.testFixture.Fixture.createMemberByIdAndName;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import roomescape.global.exception.NotFoundException;
import roomescape.member.application.dto.MemberDto;
import roomescape.member.domain.Member;
import roomescape.member.domain.repository.MemberRepository;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberService memberService;

    @DisplayName("모든 회원을 조회할 수 있다.")
    @Test
    void getAllMembers() {
        // given
        List<Member> members = List.of(
                createMemberByIdAndName(1L, "멍구"),
                createMemberByIdAndName(2L, "아이나")
        );
        given(memberRepository.findAll()).willReturn(members);

        // when
        List<MemberDto> result = memberService.getAllMembers();

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(MemberDto::name)
                .containsExactly("멍구", "아이나");

        verify(memberRepository).findAll();
    }

    @DisplayName("ID로 회원을 조회할 수 있다.")
    @Test
    void getMemberById_success() {
        // given
        Member member = createMemberByIdAndName(1L, "멍구");
        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        // when
        MemberDto result = memberService.getMemberById(1L);

        // then
        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.name()).isEqualTo("멍구");

        verify(memberRepository).findById(1L);
    }

    @DisplayName("존재하지 않는 ID로 회원 조회 시 NotFoundException이 발생한다.")
    @Test
    void getMemberById_fail() {
        // given
        Long notExistId = 99L;
        given(memberRepository.findById(notExistId)).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> memberService.getMemberById(notExistId))
                .isInstanceOf(NotFoundException.class);

        verify(memberRepository).findById(notExistId);
    }
}
