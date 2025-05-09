package roomescape.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.dao.FakeMemberDaoImpl;
import roomescape.dto.MemberRegisterRequest;
import roomescape.dto.MemberRegisterResponse;

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

}
