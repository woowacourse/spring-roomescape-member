package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AdminThemeControllerTest {

    @Test
    void 테마_추가() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams())
                .when().post("/api/v1/admin/themes")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/v1/admin/themes/1")
                .body("id", is(1));
    }

    @Test
    void 테마_삭제() {
        createTheme(themeParams());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("userName", "ADMIN"))
                .when().delete("/api/v1/admin/themes/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 존재하지_않는_테마를_삭제하면_404를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams())
                .when().delete("/api/v1/admin/themes/1")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 예약이_존재하는_테마를_삭제하면_409를_반환한다() {
        createTime("10:00");
        createTheme(themeParams());
        createReservation(reservationParams());

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("userName", "ADMIN"))
                .when().delete("/api/v1/admin/themes/1")
                .then().log().all()
                .statusCode(409);
    }

    private Map<String, Object> themeParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "이든의 공포 하우스");
        params.put("description", "이든이 귀신으로 나옴");
        params.put("imgUrl", "링크~");
        return params;
    }

    private Map<String, Object> reservationParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-12-25");
        params.put("timeId", 1);
        params.put("themeId", 1);
        return params;
    }

    private void createTime(String startAt) {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", startAt);

        RestAssured.given().contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/admin/times");
    }

    private void createTheme(Map<String, Object> params) {
        RestAssured.given().contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/v1/admin/themes");
    }

    private void createReservation(Map<String, Object> params) {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/v1/reservations");
    }
}
