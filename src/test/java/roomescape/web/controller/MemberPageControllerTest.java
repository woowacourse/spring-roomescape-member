package roomescape.web.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.support.IntegrationTestSupport;

class MemberPageControllerTest extends IntegrationTestSupport {

    @ParameterizedTest
    @ValueSource(strings = {
            "/",
            "/reservation"
    })
    @DisplayName("페이지 조회가 가능하다.")
    void accessPage(String source) {
        RestAssured.given().log().all()
                .when().get(source)
                .then().log().all()
                .statusCode(200);
    }
}
