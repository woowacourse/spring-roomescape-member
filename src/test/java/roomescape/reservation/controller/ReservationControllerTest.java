package roomescape.reservation.controller;

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
                .queryParam("name", "밀란")
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .queryParam("name", "밀란")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 이름으로_예약_목록을_조회한다() {
        Map<String, Object> params1 = new HashMap<>();
        params1.put("name", "밀란");
        params1.put("date", "2099-05-03");
        params1.put("timeId", 1L);
        params1.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params1)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> params2 = new HashMap<>();
        params2.put("name", "밀란");
        params2.put("date", "2099-05-04");
        params2.put("timeId", 2L);
        params2.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params2)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> params3 = new HashMap<>();
        params3.put("name", "브라운");
        params3.put("date", "2099-05-03");
        params3.put("timeId", 2L);
        params3.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params3)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .queryParam("name", "밀란")
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2))
                .body("[0].name", is("밀란"))
                .body("[0].date", is("2099-05-04"))
                .body("[0].time.id", is(2))
                .body("[1].name", is("밀란"))
                .body("[1].date", is("2099-05-03"))
                .body("[1].time.id", is(1));
    }

    @Test
    void 이름_없이_예약_목록을_조회하면_400을_응답한다() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("name: 입력값이 필요합니다."));
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_404를_응답한다() {
        RestAssured.given().log().all()
                .queryParam("name", "밀란")
                .when().delete("/reservations/999")
                .then().log().all()
                .statusCode(404)
                .body("message", is("존재하지 않는 예약입니다. id=999"));
    }

    @Test
    void 예약자_이름이_다르면_예약_삭제_시_403을_응답한다() {
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
                .queryParam("name", "다른이름")
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(403)
                .body("message", is("예약을 수정/삭제할 권한이 없습니다."));
    }

    @Test
    void 예약_삭제_시_이름을_입력하지_않으면_400을_응답한다() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(400)
                .body("message", is("name: 입력값이 필요합니다."));
    }

    @Test
    void 경로_변수_형식이_올바르지_않으면_400을_응답한다() {
        RestAssured.given().log().all()
                .queryParam("name", "밀란")
                .when().delete("/reservations/abc")
                .then().log().all()
                .statusCode(400)
                .body("message", is("id: 입력 형식이 잘못되었습니다."));
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
                .statusCode(409)
                .body("message", is(
                        "이미 예약이 있습니다. date= 2099-05-06, reservationTimeId= 1, themeId= 1"
                ));
    }

    @Test
    void 과거_날짜_시간으로_예약을_등록하면_422를_응답한다() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2000-01-01");
        params.put("timeId", 1L);
        params.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(422)
                .body("message", is("과거로 예약할 수 없습니다."));
    }

    @Test
    void 예약을_수정한다() {
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

        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("date", "2099-05-04");
        updateParams.put("timeId", 2L);
        updateParams.put("themeId", 2L);
        System.out.println("id = " + id);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "밀란")
                .body(updateParams)
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(200)
                .body("id", is(id))
                .body("name", is("밀란"))
                .body("date", is("2099-05-04"))
                .body("time.id", is(2))
                .body("theme.id", is(2));
    }

    @Test
    void 존재하지_않는_예약을_수정하면_404를_응답한다() {
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("date", "2099-05-04");
        updateParams.put("timeId", 2L);
        updateParams.put("themeId", 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "밀란")
                .body(updateParams)
                .when().patch("/reservations/999")
                .then().log().all()
                .statusCode(404)
                .body("message", is("존재하지 않는 예약입니다. id=999"));
    }

    @Test
    void 예약자_이름이_다르면_예약_수정_시_403을_응답한다() {
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
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("date", "2099-05-04");
        updateParams.put("timeId", 1L);
        updateParams.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "다른이름")
                .body(updateParams)
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(403)
                .body("message", is("예약을 수정/삭제할 권한이 없습니다."));
    }

    @Test
    void 존재하지_않는_예약시간으로_수정하면_404를_응답한다() {
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
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("timeId", 999L);
        updateParams.put("date", "2099-05-03");
        updateParams.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "밀란")
                .body(updateParams)
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(404)
                .body("message", is("존재하지 않는 예약 시간입니다. id=999"));
    }

    @Test
    void 존재하지_않는_테마로_수정하면_404를_응답한다() {
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
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("themeId", 999L);
        updateParams.put("date", "2099-05-03");
        updateParams.put("timeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "밀란")
                .body(updateParams)
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(404)
                .body("message", is("존재하지 않는 테마입니다. id=999"));
    }

    @Test
    void 과거_날짜_시간으로_수정하면_422를_응답한다() {
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
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("date", "2000-01-01");
        updateParams.put("timeId", 1L);
        updateParams.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "밀란")
                .body(updateParams)
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(422)
                .body("message", is("과거로 예약할 수 없습니다."));
    }

    @Test
    void 이미_예약된_날짜_시간_테마로_수정하면_409를_응답한다() {
        Map<String, Object> params1 = new HashMap<>();
        params1.put("name", "밀란");
        params1.put("date", "2099-05-03");
        params1.put("timeId", 1L);
        params1.put("themeId", 1L);
        Map<String, Object> params2 = new HashMap<>();
        params2.put("name", "브라운");
        params2.put("date", "2099-05-04");
        params2.put("timeId", 2L);
        params2.put("themeId", 1L);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params1)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);
        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params2)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");
        Map<String, Object> updateParams = new HashMap<>();
        updateParams.put("date", "2099-05-03");
        updateParams.put("timeId", 1L);
        updateParams.put("themeId", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("name", "브라운")
                .body(updateParams)
                .when().patch("/reservations/" + id)
                .then().log().all()
                .statusCode(409)
                .body("message", is(
                        "이미 예약이 있습니다. date= 2099-05-03, reservationTimeId= 1, themeId= 1"
                ));
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
