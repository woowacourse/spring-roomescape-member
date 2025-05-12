package roomescape.controller.member;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.config.BypassAuthInterceptorConfig;
import roomescape.dto.member.MemberResponse;
import roomescape.infrastructure.config.WebMvcConfig;
import roomescape.service.memeber.MemberService;

@WebMvcTest(controllers = AdminMemberController.class)
@Import(BypassAuthInterceptorConfig.class)
class AdminMemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;

    @MockitoBean
    private WebMvcConfig webMvcConfig;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @DisplayName("관리자가 모든 회원 목록을 조회한다.")
    void readAllMembers() {
        MemberResponse response1
                = new MemberResponse(1L, "user1");
        MemberResponse response2
                = new MemberResponse(2L, "user2");

        List<MemberResponse> responses = List.of(
                response1, response2
        );
        given(memberService.getAll()).willReturn(responses);

        RestAssuredMockMvc.given().log().all()
                .when().get("/admin/members")
                .then().log().all()
                .status(HttpStatus.OK)
                .body("size()", is(2))
                .body("[0].id", is(1))
                .body("[0].name", is("user1"))
                .body("[1].id", is(2))
                .body("[1].name", is("user2"));
    }
}
