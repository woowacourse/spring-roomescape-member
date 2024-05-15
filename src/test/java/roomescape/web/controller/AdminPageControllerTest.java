package roomescape.web.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.support.IntegrationTestSupport;

class AdminPageControllerTest extends IntegrationTestSupport {

    @ParameterizedTest
    @ValueSource(strings = {
            "/admin",
            "/admin/reservation",
            "/admin/time",
            "/admin/theme"
    })
    @DisplayName("페이지 조회가 가능하다.")
    void accessPage(String source) {
        RestAssured.given().log().all()
                .cookie("token", getAdminToken())
                .when().get(source)
                .then().log().all()
                .statusCode(200);
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "/admin",
            "/admin/reservation",
            "/admin/time",
            "/admin/theme"
    })
    @DisplayName("어드민만 접근 가능하다.")
    void onlyAdmin(String source) {
        RestAssured.given().log().all()
                .cookie("token", getMemberToken())
                .when().get(source)
                .then().log().all()
                .statusCode(401)
                .body("message", is("유효한 인가 정보를 입력해주세요."));
    }
}
