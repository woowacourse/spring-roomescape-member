package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationControllerTest extends ControllerTest {

    @DisplayName("사용자 예약 추가")
    @Test
    void 사용자_예약_추가_API() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @DisplayName("사용자 예약 삭제")
    @Test
    void 사용자_예약_삭제() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .pathParam("id", 1)
                .when().delete("/reservations/{id}")
                .then().log().all()
                .statusCode(204);
    }

    @DisplayName("사용자 예약 조회")
    @Test
    void 사용자_예약_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("username", "김철수")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200);
    }
}
