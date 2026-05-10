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
public class ThemeAcceptanceTest {
    @Test
    void reservationTimeApiSuccessTest() {
        // 1. 테마 추가
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

        // 2. 테마 조회
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        // 3. 시간 추가
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/admin/times")
                .then().log().all()
                .statusCode(201);

        // 4. 예약 추가
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

        // 5. 특정 기간 내의 테마 랭킹 조회
        RestAssured.given().log().all()
                .param("start-date", "2026-05-01")
                .param("end-date", "2026-05-07")
                .when().get("/themes/ranking")
                .then().log().all()
                .statusCode(200)
                .body("[0].id", is(1));

        // 5. 예약 삭제
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        // 6. 테마 삭제
        RestAssured.given().log().all()
                .when().delete("/admin/themes/1")
                .then().log().all()
                .statusCode(204);

        // 6. 전체 테마 조회로 테마 삭제 확인
        RestAssured.given().log().all()
                .when().get("/themes")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }
}
