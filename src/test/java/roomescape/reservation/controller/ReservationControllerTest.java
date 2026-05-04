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
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationControllerTest {

    @Test
    void 예약을_추가한다() {
        createReservationTime();
        createTheme();

        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2026-05-03");
        params.put("timeId", 1);
        params.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("name", is("밀란"))
                .body("date", is("2026-05-03"))
                .body("time.id", is(1))
                .body("theme.id", is(1));
    }


    @Test
    void 예약_목록을_조회한다() {
        createReservationTime();
        createTheme();

        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2026-05-03");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("밀란"))
                .body("[0].date", is("2026-05-03"))
                .body("[0].time.id", is(1));
    }

    @Test
    void 예약을_삭제한다() {
        createReservationTime();
        createTheme();

        Map<String, Object> params = new HashMap<>();
        params.put("name", "밀란");
        params.put("date", "2026-05-03");
        params.put("timeId", 1);

        Integer id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(200)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().delete("/reservations/" + id)
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("find { it.id == " + id + " }", is((Object) null));
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_404를_응답한다() {
        RestAssured.given().log().all()
                .when().delete("/reservations/999")
                .then().log().all()
                .statusCode(404);
    }

    @ParameterizedTest(name = "{0}은 1에서 10자 이내의 예약자 이름이 아니다")
    @ValueSource(strings = {"", "12345678901"})
    void 예약을_추가할_때_이름이_1자에서_10자이내가_아니면_400을_응답한다(String name) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("date", "2023-08-05");
        params.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400);
    }

    private static void createTheme() {
        Map<String, Object> theme = new HashMap<>();
        theme.put("name", "테마A");
        theme.put("description", "테마A란...");
        theme.put("thumbnailUrl", "www.d.d.d");
        theme.put("runtime", 1L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(200);
    }

    private static void createReservationTime() {
        Map<String, Object> reservationTime = new HashMap<>();
        reservationTime.put("startAt", "12:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(200);
    }
}
