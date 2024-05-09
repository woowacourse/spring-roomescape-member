package roomescape.member;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class MemberControllerTest {

    @Test
    @DisplayName("방탈출 사용자 예약 페이지를 매핑한다")
    void reservation() {
        RestAssured.given().log().all()
                .when().get("/member/reservation")
                .then().log().all()
                .statusCode(200);
    }
}
