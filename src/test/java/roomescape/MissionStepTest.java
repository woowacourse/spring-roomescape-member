package roomescape;

import static org.hamcrest.Matchers.is;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class MissionStepTest {

    @LocalServerPort
    int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

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
        params.put("date", "2099-08-05");
        params.put("themeId", 1);
        params.put("timeId", 1);

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
    void 같은_날짜와_시간이라도_테마가_다르면_각각_예약_가능하다() {
        Map<String, String> theme1 = new HashMap<>();
        theme1.put("name", "테마1");
        theme1.put("description", "설명1");
        theme1.put("imageUrl", "https://example.com/theme1.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme1)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        Map<String, String> theme2 = new HashMap<>();
        theme2.put("name", "테마2");
        theme2.put("description", "설명2");
        theme2.put("imageUrl", "https://example.com/theme2.png");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(theme2)
                .when().post("/themes")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2));

        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");
        time.put("endAt", "16:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        Map<String, Object> reservation1 = new HashMap<>();
        reservation1.put("name", "브라운");
        reservation1.put("date", "2099-08-05");
        reservation1.put("themeId", 1);
        reservation1.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation1)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        Map<String, Object> reservation2 = new HashMap<>();
        reservation2.put("name", "초코");
        reservation2.put("date", "2099-08-05");
        reservation2.put("themeId", 2);
        reservation2.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation2)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2));

        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    void 없는_예약_삭제시_404_에러_응답() {
        RestAssured.given().log().all()
            .when().delete("/reservations/999")
            .then().log().all()
            .statusCode(404)
            .body("code", is("RESERVATION_NOT_FOUND"))
            .body("message", is("예약이 존재하지 않습니다."));
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
        reservation.put("date", "2099-08-05");
        reservation.put("themeId", 1);
        reservation.put("timeId", 1);

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
        reservation.put("date", "2099-08-05");
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("code", is("INVALID_REQUEST"))
                .body("message", is("요청이 올바르지 않습니다."));
    }


    @Test
    void 지난_날짜_시간_예약은_거부한다() {
        createThemeAndTime();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2020-08-05");
        reservation.put("themeId", 1);
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(400)
                .body("code", is("PAST_RESERVATION_NOT_ALLOWED"))
                .body("message", is("지난 날짜와 시간으로 예약할 수 없습니다."));
    }

    @Test
    void 같은_날짜_시간_테마_중복_예약은_거부한다() {
        createThemeAndTime();

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2099-08-05");
        reservation.put("themeId", 1);
        reservation.put("timeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(409)
                .body("code", is("DUPLICATE_RESERVATION"))
                .body("message", is("이미 예약된 시간입니다."));
    }

    private void createThemeAndTime() {
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
    }

}
