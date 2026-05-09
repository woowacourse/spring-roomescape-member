package roomescape.reservation.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

@Sql({"/create_reservation_time.sql", "/create_theme.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationControllerTest {

    @Test
    void 예약을_추가한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2026-05-03");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", containsString("/reservations/"))
                .body("name", is("밀란"))
                .body("date", is("2026-05-03"))
                .body("time.id", is(1))
                .body("theme.id", is(1));
    }

    @Test
    void 예약_목록을_조회한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2026-05-03");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("name", hasItem("밀란"))
                .body("date", hasItem("2026-05-03"))
                .body("time.id", hasItem(1));
    }

    @Test
    void 예약을_삭제한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2026-05-03");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_404를_응답한다() {
        RestAssured.given().log().all()
                .when().delete("/reservations/999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 날짜_시간_테마가_같은_예약을_등록요청하면_409를_응답한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2026-05-06");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409);
    }

    @ParameterizedTest(name = "{0}은 올바른 예약자 이름이 아니다")
    @CsvSource(value = {"'':예약자 이름은 필수입니다.", "12345678901:예약자 이름은 10자 이하입니다."}, delimiter = ':')
    void 예약을_추가할_때_이름이_올바르지_않으면_400과_예외_메시지를_응답한다(String name, String expectedMessage) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", "2023-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body(containsString(expectedMessage));
    }

}
