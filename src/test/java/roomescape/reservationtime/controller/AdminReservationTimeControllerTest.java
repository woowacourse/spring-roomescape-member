package roomescape.reservationtime.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql("/create_reservation_time.sql")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class AdminReservationTimeControllerTest {

    @Test
    void 예약_시간을_추가한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "11:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .body("startAt", is("11:00:00"));
    }

    @Test
    void 예약_시간을_삭제한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", "11:00");

        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().delete("/admin/times/" + id)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 존재하지_않는_예약_시간을_삭제하면_404를_응답한다() {
        RestAssured.given().log().all()
                .when().delete("/admin/times/999")
                .then().log().all()
                .statusCode(404);
    }

    @Sql("/create_reservation_time.sql")
    @Test
    void 예약이_존재하는_예약_시간을_삭제하면_409를_응답한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "봉구스");
        params.put("date", "2099-05-06");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(409);
    }

    @ParameterizedTest(name = "{0}은 예약 시간 형식이 아니다")
    @ValueSource(strings = {"", "abc", "25:00"})
    void 예약_시간을_추가할_때_시간_형식이_올바르지_않으면_400을_응답한다(String startAt) {
        Map<String, Object> params = new HashMap<>();
        params.put("startAt", startAt);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400);
    }

}
