package roomescape.service.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.member.FakeMemberDaoImpl;
import roomescape.dto.member.request.MemberRegisterRequest;
import roomescape.dto.member.response.MemberRegisterResponse;
import roomescape.dto.member.response.MemberResponse;

class MemberServiceTest {

    private MemberService memberService;

    @BeforeEach
    void setUp() {
        this.memberService = new MemberService(new FakeMemberDaoImpl());
    }

    @DisplayName("회원가입에 성공하면 회원 정보를 담은 응답을 반환한다.")
    @Test
    void register() {
        //given
        MemberRegisterRequest request = new MemberRegisterRequest("testName", "testEmail", "1234");

        //when
        MemberRegisterResponse actual = memberService.register(request);

        //then
        assertThat(actual)
                .extracting("id", "name", "email")
                .containsExactly(1L, "testName", "testEmail");
    }

    @DisplayName("저장된 모든 member를 조회한다.")
    @Test
    void findAll() {
        //given
        MemberRegisterRequest request1 = new MemberRegisterRequest("testName1", "testEmail1", "1234");
        memberService.register(request1);

        MemberRegisterRequest request2 = new MemberRegisterRequest("testName1", "testEmail2", "1234");
        memberService.register(request2);

        //when
        List<MemberResponse> actual = memberService.findAll();

        //then
        assertThat(actual).hasSize(2);
    }

}
