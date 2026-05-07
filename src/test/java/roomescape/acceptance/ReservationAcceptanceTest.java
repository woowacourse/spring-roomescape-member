package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationAcceptanceTest {
    @Test
    void reservationTimeApiSuccessTest() {
        // 1. 시간 추가
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        // 2. 테마 추가
        Map<String, String> theme = new HashMap<>();
        theme.put("name", "방탈출1");
        theme.put("description", "방탈출1 설명");
        theme.put("thumbnailUrl", "theme/url.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);

        // 3. 예약 추가
        Map<String, String> reservation = new HashMap<>();
        reservation.put("name", "예약자");
        reservation.put("date", "2026-05-01");
        reservation.put("timeId", "1");
        reservation.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // 4. 전체 예약 조회
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        // 5. 예약 삭제
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        // 6. 전체 예약 조회로 예약 삭제 확인
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void reservationTimeApiFailTest() {
        // 1. 시간 추가
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        // 2. 테마 추가
        Map<String, String> theme = new HashMap<>();
        theme.put("name", "방탈출1");
        theme.put("description", "방탈출1 설명");
        theme.put("thumbnailUrl", "theme/url.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/admin/themes")
                .then().log().all()
                .statusCode(201);

        // 3. 예약 추가
        Map<String, String> reservation = new HashMap<>();
        reservation.put("name", "예약자");
        reservation.put("date", "2026-05-01");
        reservation.put("timeId", "1");
        reservation.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        // 4. 날짜, 시간, 테마가 동일한 예약 추가 시 예외 발생
        Map<String, String> duplicatedReservation = new HashMap<>();
        duplicatedReservation.put("name", "예약자");
        duplicatedReservation.put("date", "2026-05-01");
        duplicatedReservation.put("timeId", "1");
        duplicatedReservation.put("themeId", "1");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(duplicatedReservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("message", is("[ERROR] 동일한 예약이 이미 존재합니다."));
    }
}
