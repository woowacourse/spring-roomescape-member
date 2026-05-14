package roomescape.reservation.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @Test
    void 이름으로_예약_조회_성공() {
        RestAssured.given().log().all()
                .when().get("/api/reservations?name=도우너")
                .then().log().all()
                .statusCode(200);
    }

    @Test
    void 예약추가_예약존재_성공() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "초록");
        params.put("date", "2026-05-05");
        params.put("timeId", 7L);
        params.put("themeId", 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(201);
    }

    @Test
    void 예약_추가_시간없음_실패() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "초록");
        params.put("date", "2026-05-05");
        params.put("timeId", 15L);
        params.put("themeId", 2L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/reservations")
                .then().log().all()
                .statusCode(500);
    }

    @Test
    void 예약_변경_성공() {
        Map<String, Object> params = new HashMap<>();
        params.put("date", "2026-05-14");
        params.put("timeId", 7L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/api/reservations/2?name=도우너")
                .then().log().all()
                .statusCode(200);

        RestAssured.given().log().all()
                .when().get("/api/reservations?name=도우너")
                .then().log().all()
                .body("reservations[0].date", is("2026-05-14"))
                .body("reservations[0].time.id", is(7));
    }

    @Test
    void 예약_삭제_성공() {
        RestAssured.given().log().all()
                .when().delete("/api/reservations/14?name=도우너")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/api/reservations?name=도우너")
                .then().log().all()
                .body("count", is(1));
    }

    @Test
    void 과거_예약_취소_테스트() {
        RestAssured.given().log().all()
                .when().delete("/api/reservations/2?name=도우너")
                .then().log().all()
                .statusCode(422);
    }

    @Test
    void 본인_예약이_아닌_예약_삭제_테스트() {
        RestAssured.given().log().all()
                .when().delete("/api/reservations/2?name=브라운")
                .then().log().all()
                .statusCode(403);
    }
}
