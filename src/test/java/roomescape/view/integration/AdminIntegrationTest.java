package roomescape.view.integration;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.model.IntegrationTest;

public class AdminIntegrationTest extends IntegrationTest {

    @Test
    @DisplayName("관리자 메인 페이지가 잘 접속된다.")
    void adminMainPageLoad() {
        RestAssured.given().log().all()
                .cookie(cookie.toString())
                .when().get("/admin")
                .then().log().all()
                .statusCode(200);
    }
}
