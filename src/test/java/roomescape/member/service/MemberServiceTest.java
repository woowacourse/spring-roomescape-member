package roomescape.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.exception.custom.ExistedDuplicateValueException;
import roomescape.member.domain.Member;
import roomescape.member.service.dto.CreateMemberServiceRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("멤버 정보를 저장한 다음 저장 정보를 리턴한다")
    void addMemberTest() {
        // given
        final CreateMemberServiceRequest creation =
                new CreateMemberServiceRequest("razel@email.com", "razelpassword", "razel");

        // when
        final Member actual = memberService.addMember(creation);

        // then
        assertAll(
                () -> assertThat(memberService.findAllMembers()).hasSize(3),
                () -> assertThat(actual.getId()).isEqualTo(3L)
        );
    }

    @Test
    @DisplayName("동일한 이메일을 사용한 계정이 존재하는 경우 예외를 던진다")
    void addMemberTest_WhenDuplicatedEmailExists() {
        // given
        final CreateMemberServiceRequest duplicated =
                new CreateMemberServiceRequest("user@email.com", "razelpassword", "razel");

        // when // then
        assertThatThrownBy(() -> memberService.addMember(duplicated))
                .isInstanceOf(ExistedDuplicateValueException.class)
                .hasMessageContaining("이미 존재하는 이메일입니다: " + duplicated.email());
    }

    @Test
    @DisplayName("예약을 조회한다")
    void findReservationsTest() {
        // when
        final List<Member> actual = memberService.findAllMembers();

        // then
        assertThat(actual).hasSize(2);
    }
}
