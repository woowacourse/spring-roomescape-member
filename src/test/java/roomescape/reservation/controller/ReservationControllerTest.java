package roomescape.reservation.controller;

import static org.hamcrest.Matchers.hasItem;
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

@Sql({"/create_reservation_time.sql", "/create_theme.sql"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationControllerTest {

    @Test
    void 예약을_추가한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2099-05-03");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("name", is("밀란"))
                .body("date", is("2099-05-03"))
                .body("time.id", is(1))
                .body("theme.id", is(1));
    }

    @Test
    void 예약_목록을_조회한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2099-05-03");
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
                .body("date", hasItem("2099-05-03"))
                .body("time.id", hasItem(1));
    }

    @Test
    void 예약을_삭제한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2099-05-03");
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
        params.put("date", "2099-05-06");
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

    @ParameterizedTest(name = "{0}은 1에서 10자 이내의 예약자 이름이 아니다")
    @ValueSource(strings = {"", "12345678901"})
    void 예약을_추가할_때_이름이_1자에서_10자이내가_아니면_400을_응답한다(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", "2099-08-05");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

}
