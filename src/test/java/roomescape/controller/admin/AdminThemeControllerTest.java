package roomescape.controller.admin;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.ReservationController;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminThemeControllerTest {
    @Autowired
    private ReservationController reservationController;

    @Test
    void 테마_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "추리물");
        params.put("description", "추리");
        params.put("url", "/reasoning");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/theme")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().delete("/admin/theme/16")
                .then().log().all()
                .statusCode(204);
    }
}
