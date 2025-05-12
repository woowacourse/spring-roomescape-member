package roomescape.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import roomescape.auth.dto.MemberProfileResponse;
import roomescape.global.exception.error.NotFoundException;
import roomescape.member.domain.Member;
import roomescape.member.domain.enums.Role;
import roomescape.member.dto.MemberResponse;
import roomescape.reservation.repository.fake.MemberFakeRepository;


class MemberServiceTest {

    private MemberService memberService;
    private MemberFakeRepository memberRepository;

    @BeforeEach
    void setup() {
        memberRepository = new MemberFakeRepository();

        memberRepository.save(new Member(null, "루키", "rookie123@woowa.com", "rookierookie123", Role.USER));
        memberRepository.save(new Member(null, "하루", "haru123@woowa.com", "haruharu123", Role.USER));
        memberRepository.save(new Member(null, "베루스", "verus@woowa.com", "verusverus123", Role.ADMIN));

        memberService = new MemberService(memberRepository);
    }

    @DisplayName("전체 사용자를 조회한다")
    @Test
    void find_all_test() {
        // when
        List<MemberResponse> members = memberService.findAll();

        // then
        assertThat(members).containsExactlyInAnyOrder(
                new MemberResponse(1L, "루키", "rookie123@woowa.com"),
                new MemberResponse(2L, "하루", "haru123@woowa.com"),
                new MemberResponse(3L, "베루스", "verus@woowa.com")
        );
    }

    @DisplayName("사용자의 ID로 프로필 정보를 조회한다")
    @CsvSource({
            "1, 루키",
            "2, 하루",
            "3, 베루스"
    })
    @ParameterizedTest
    void find_member_profile_test(Long memberId, String expectedName) {
        // when
        MemberProfileResponse findMemberProfile = memberService.findMemberProfile(memberId);

        // then
        assertThat(findMemberProfile.name()).isEqualTo(expectedName);
    }

    @DisplayName("사용자의 ID에 해당하는 데이터가 존재하지 않으면 예외가 발생한다")
    @Test
    void find_member_profile_exception_test() {
        // given
        Long findId = 999L;

        // when & then
        assertThatThrownBy(() -> memberService.findMemberProfile(findId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("존재하지 않는 사용자입니다.");
    }

}
