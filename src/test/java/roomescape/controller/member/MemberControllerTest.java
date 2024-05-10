package roomescape.controller.member;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MemberControllerTest {

    @Autowired
    MemberController memberController;

    @Test
    @DisplayName("멤버 조회")
    void getMembers() {
        RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200);
    }
}
