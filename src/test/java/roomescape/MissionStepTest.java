package roomescape;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.controller.ReservationController;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Autowired
    private ReservationController reservationController;


    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        int timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("times.find { it.id == " + timeId + " }.startAt", is("10:00"));

        RestAssured.given().log().all()
                .when().delete("/admin/times/" + timeId)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 중복된_예약_시간을_추가하면_bad_request를_반환한다() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 존재하지_않는_API는_not_found를_반환한다() {
        RestAssured.given().log().all()
                .when().get("/unknown-api")
                .then().log().all()
                .statusCode(404)
                .body("message", is("요청한 API를 찾을 수 없습니다."));
    }

    @Test
    void 지원하지_않는_HTTP_메서드는_method_not_allowed를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(new HashMap<>())
                .when().post("/times")
                .then().log().all()
                .statusCode(405)
                .body("message", is("지원하지 않는 HTTP 메서드입니다."));
    }

    @Test
    void 예약과_시간_연결() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        Map<String, String> theme = new HashMap<>();
        theme.put("name", "이름");
        theme.put("description", "내용");
        theme.put("thumbnail", "https://example.com/theme.png");

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        int timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        int themeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("time.id", is(timeId))
                .body("theme.id", is(themeId));

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("reservations.find { it.time.id == " + timeId + " }.theme.id", is(themeId));
    }

    @Test
    void 예약_가능_시간_조회_후_예약하면_해당_시간이_예약_불가가_된다() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "23:00");

        Map<String, String> theme = new HashMap<>();
        theme.put("name", "예약 가능 시간 테스트");
        theme.put("description", "예약 가능 시간 테스트용 테마");
        theme.put("thumbnail", "https://example.com/availability-theme.png");

        int timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        int themeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201)
                .extract()
                .path("id");

        RestAssured.given().log().all()
                .when().get("/times/availability?date=2026-06-01&themeId=" + themeId)
                .then().log().all()
                .statusCode(200)
                .body("availableTimes.find { it.id == " + timeId + " }.isAvailable", is(true));

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2026-06-01");
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times/availability?date=2026-06-01&themeId=" + themeId)
                .then().log().all()
                .statusCode(200)
                .body("availableTimes.find { it.id == " + timeId + " }.isAvailable", is(false));
    }

    @Test
    void 계층화_리팩터링() {
        boolean isJdbcTemplateInjected = false;

        for (Field field : reservationController.getClass().getDeclaredFields()) {
            if (field.getType().equals(JdbcTemplate.class)) {
                isJdbcTemplateInjected = true;
                break;
            }
        }

        assertThat(isJdbcTemplateInjected).isFalse();
    }
}
