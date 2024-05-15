package roomescape.web.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.support.IntegrationTestSupport;

class MemberControllerTest extends IntegrationTestSupport {

    @Test
    @DisplayName("전체 사용자 목록을 조회한다.")
    void getAll() {
        RestAssured.given().log().all()
                .when().get("/members")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }
}
