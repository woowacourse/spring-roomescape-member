package roomescape.reservation.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Test
    @DisplayName("예약 생성 성공")
    void 예약_생성_성공() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "현미밥", "date", "2026-08-05", "timeId", 1, "themeId", 1))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    @DisplayName("존재하지 않는 timeId로 예약 생성 시 404")
    void 존재하지_않는_timeId_예약_생성_실패() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "현미밥", "date", "2026-08-05", "timeId", 999, "themeId", 1))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("존재하지 않는 themeId로 예약 생성 시 404")
    void 존재하지_않는_themeId_예약_생성_실패() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(Map.of("name", "현미밥", "date", "2026-08-05", "timeId", 1, "themeId", 999))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    @DisplayName("예약 삭제 성공")
    void 예약_삭제_성공() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }
}
