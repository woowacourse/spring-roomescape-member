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
public class ReservationTimeAcceptanceTest {

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

        // 2. 전체 시간 조회
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        // 3. 테마 추가
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

        // 4. 특정 날짜, 테마의 예약 가능한 시간 조회
        RestAssured.given().log().all()
                .queryParam("date", "2026-05-01")
                .queryParam("themeId", "1")
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .body("[0].available", is(true));

        // 5. 예약 추가
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

        // 6. 예약 추가 후 해당 날짜, 테마의 시간 예약 가능 상태 변경 조회
        RestAssured.given().log().all()
                .queryParam("date", "2026-05-01")
                .queryParam("themeId", "1")
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .body("[0].available", is(false));

        // 7. 예약 삭제
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        // 8. 시간 삭제
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);

        // 9. 전체 시간 조회로 시간 삭제 확인
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
