package roomescape.admin;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class AdminReservationTimeControllerTest {

    @Test
    void 예약시간_관리() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "20:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/admin/times/6")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 빈값으로_예약시간_추가시_400() {
        Map<String, Object> params = new HashMap<>();
        params.put("time", null);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 존재하지_않는_예약시간_삭제시_404() {
        RestAssured.given().log().all()
                .when().delete("/admin/times/0")
                .then().log().all()
                .statusCode(404);
    }
}
