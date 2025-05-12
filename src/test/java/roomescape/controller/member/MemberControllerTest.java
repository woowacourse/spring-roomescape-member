package roomescape.controller.member;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;

import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import roomescape.dto.MemberRequest;
import roomescape.dto.member.MemberResponse;
import roomescape.infrastructure.config.WebMvcConfig;
import roomescape.service.memeber.MemberService;

@WebMvcTest(controllers = MemberController.class)
class MemberControllerTest {

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
    @DisplayName("사용자가 예약 가능한 시간을 조회한다.")
    void getAvailableReservationTimes() {
        long expectedId = 1L;
        String name = "테스트";
        String email = "test1234@example.com";
        String password = "test1234";

        MemberRequest dto = new MemberRequest(name, email, password);

        MemberResponse response = new MemberResponse(expectedId, name);
        given(memberService.addMember(dto)).willReturn(response);

        RestAssuredMockMvc.given().log().all()
                .contentType(ContentType.JSON)
                .body(dto)
                .when().post("/members")
                .then().log().all()
                .status(HttpStatus.CREATED)
                .body("id", is((int) expectedId))
                .body("name", is(name));
    }
}
