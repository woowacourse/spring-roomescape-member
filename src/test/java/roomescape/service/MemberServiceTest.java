package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import roomescape.domain.Member;
import roomescape.dto.MemberRegisterRequest;
import roomescape.dto.MemberRegisterResponse;
import roomescape.dto.MemberResponse;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("회원을 성공적으로 저장 한다.")
     void saveTest() {
        //given
        final MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest("test", "test", "test");

        //when
        final MemberRegisterResponse expected = memberService.addMember(memberRegisterRequest);

        //then
        assertAll(
                () -> assertThat(expected.name()).isEqualTo(memberRegisterRequest.name()),
                () -> assertThat(expected.email()).isEqualTo(memberRegisterRequest.email())
        );
    }

    @Test
    @DisplayName("모든 멤버를 성공적으로 가져온다.")
    void getAllMembersTest() {
        //given
        final MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest("test12", "test12", "test12");
        memberService.addMember(memberRegisterRequest);

        //when
        final List<MemberResponse> expected = memberService.getAllMembers();

        //then
        assertThat(expected).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("id로 멤버를 성공적으로 가져온다.")
    void getMemberById() {
        //given
        final MemberRegisterRequest memberRegisterRequest = new MemberRegisterRequest("test123", "test123", "test123");
        final MemberRegisterResponse saved = memberService.addMember(memberRegisterRequest);

        //when
        final Member expected = memberService.getMemberById(saved.id());

        //then
        assertAll(
                () -> assertThat(expected.getId()).isEqualTo(saved.id()),
                () -> assertThat(expected.getEmail()).isEqualTo(saved.email()),
                () -> assertThat(expected.getName()).isEqualTo(saved.name())
        );

    }


}
