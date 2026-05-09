package roomescape;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class MissionStepTest {

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0)); // 아직 생성 요청이 없으니 0개
    }

    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .when().get("/admin/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/admin/times/1")
                .then().log().all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @Test
    void 예약과_시간_연결() {
        Map<String, Object> time = new HashMap<>();
        time.put("startAt", "10:00");

        int timeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract().path("id");

        Map<String, Object> theme = new HashMap<>();
        theme.put("name", "테마1");
        theme.put("thumbnailUrl", "http://url");
        theme.put("description", "난이도 보통 3/5");

        int themeId = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .extract().body().path("id");

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("timeId", timeId);
        reservation.put("themeId", themeId);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    void 존재하지_않는_예약을_삭제하면_404을_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/admin/reservations/999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 존재하지_않는_시간을_삭제하면_404을_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/admin/times/999")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 존재하지_않는_시간으로_예약하면_400을_반환한다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "3000-08-05");
        reservation.put("timeId", 999);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 이름이_빈_문자열이면_400을_반환한다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "");
        reservation.put("date", "3000-08-05");
        reservation.put("timeId", 1);   // timeId같은 경우 어떤게 들어가도 테스트에 지장이 가지 않는다.

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 이름이_null이면_400을_반환한다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("date", "3000-08-05");
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 잘못된_날짜_형식이면_400을_반환한다() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "이상한값");
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    void 잘못된_시간_형식이면_400을_반환한다() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "이상한값");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(400);
    }
}
