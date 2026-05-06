package roomescape;

import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0)); // 아직 생성 요청이 없으니 0개
    }

    @Test
    void 예약_추가_및_삭제() {
        Map<String, String> theme = new HashMap<>();
        theme.put("name", "테마");
        theme.put("description", "설명");
        theme.put("imageUrl", "https://example.com/theme.png");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(theme)
            .when().post("/themes")
            .then().log().all()
            .statusCode(201)
            .body("id", is(1));

        Map<String, String> time = new HashMap<>();
        time.put("startAt", "15:40");
        time.put("endAt", "18:00");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(time)
            .when().post("/times")
            .then().log().all()
            .statusCode(201)
            .body("id", is(1));

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("themeId", 1);
        params.put("time", "15:40");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(201)
            .body("id", is(1));

        RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(1));

        RestAssured.given().log().all()
            .when().delete("/reservations/1")
            .then().log().all()
            .statusCode(204);

        RestAssured.given().log().all()
            .when().get("/reservations")
            .then().log().all()
            .statusCode(200)
            .body("size()", is(0));
    }

    @Test
    void 없는_예약_삭제시_404_에러_응답() {
        RestAssured.given().log().all()
            .when().delete("/reservations/999")
            .then().log().all()
            .statusCode(404)
            .body("code", is("RESERVATION_NOT_FOUND"))
            .body("message", is("예약이 존재하지 않습니다. id=999"));
    }

    @Test
    void 시간_관리_API() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");
        params.put("endAt", "16:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));

        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 예약과_시간_연결() {
        Map<String, String> theme = new HashMap<>();
        theme.put("name", "테마");
        theme.put("description", "설명");
        theme.put("imageUrl", "https://example.com/theme.png");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201);

        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");
        time.put("endAt", "16:00");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(201);

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("themeId", 1);
        reservation.put("time", "10:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].time.id", is(1))
                .body("[0].time.startAt", is("10:00"));
    }

    @Test
    void 예약_시간이_없으면_400_에러_응답() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2023-08-05");
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_REQUEST"))
                .body("message", is("예약 시간은 필수입니다."));
    }

}
