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
public class ReservationControllerTest {

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 예약_추가() {
        createDefaultTimes();
        createDefaultThemes();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    @Test
    void 예약_추가_조회() {
        createDefaultTimes();
        createDefaultThemes();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", is(1))
                .body("[0].name", is("브라운"))
                .body("[0].date", is("2023-08-05"))
                .body("[0].time.id", is(1))
                .body("[0].time.startAt", is("10:00"));
    }

    @Test
    void 예약_추가_및_삭제() {
        createDefaultTimes();
        createDefaultThemes();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/api/v1/reservations/1")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_404를_반환한다() {
        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));

        RestAssured.given().log().all()
                .when().delete("/api/v1/reservations/1")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 사용자는_예약_가능한_시간을_선택하여_예약_가능하다() {
        createDefaultTimes();
        createDefaultThemes();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("timeId", 2L)))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 사용자가_예약된_시간을_선택하여_예약할경우_409를_반환한다() {
        createDefaultTimes();
        createDefaultThemes();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("timeId", 1L)))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(409);
    }

    private void createDefaultTimes() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/admin/times")
                .then().statusCode(201);

        Map<String, String> time2 = new HashMap<>();
        time2.put("startAt", "11:00");

        RestAssured.given().contentType(ContentType.JSON)
                .body(time2)
                .when().post("/api/v1/admin/times")
                .then().statusCode(201);

        Map<String, String> time3 = new HashMap<>();
        time3.put("startAt", "12:00");

        RestAssured.given().contentType(ContentType.JSON)
                .body(time3)
                .when().post("/api/v1/admin/times")
                .then().statusCode(201);
    }

    private void createDefaultThemes() {
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", "이든의 공포 하우스");
        themeParams.put("description", "이든이 귀신으로 나옴");
        themeParams.put("imgUrl", "링크~");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes")
                .then().statusCode(201);

        Map<String, Object> themeParams2 = new HashMap<>();
        themeParams2.put("name", "정콩이의 방탈출");
        themeParams2.put("description", "니는 못나간다");
        themeParams2.put("imgUrl", "링크~");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams2)
                .when().post("/api/v1/admin/themes")
                .then().statusCode(201);
    }

    private Map<String, Object> reservationParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", 1L);
        params.put("themeId", 1L);
        return params;
    }

    private Map<String, Object> reservationParams(Map<String, Object> overrides) {
        Map<String, Object> params = reservationParams();
        params.putAll(overrides);
        return params;
    }
}
