package roomescape.controller;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.dto.MemberResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberRestControllerTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    private static List<MemberResponse> memberDatas = List.of(
            new MemberResponse(1L, "어드민"),
            new MemberResponse(2L, "사용자1"),
            new MemberResponse(3L, "사용자2"),
            new MemberResponse(4L, "사용자3")
    );

    @Test
    @DisplayName("모든 회원을 조회한다.")
    void getAll() {
        // given && when
        List<MemberResponse> members = RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200).extract()
                .jsonPath().getList(".", MemberResponse.class);

        // then
        assertThat(members).isEqualTo(memberDatas);
    }
}
