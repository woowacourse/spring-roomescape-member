package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    @Test
    void 예약_조회시_userName이_없으면_200을_반환한다() {
        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 예약_조회시_userName이_있으면_해당_이름의_예약만_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("name", "브라운", "timeId", 1L)))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("name", "정콩이", "timeId", 2L)))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201);

        RestAssured.given().log().all()
                .when().get("/api/v1/reservations?userName=브라운")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].name", is("브라운"));
    }

    @Test
    void 예약_생성시_성공하면_201을_반환한다() {
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
    void 예약_조회시_생성된_예약을_반환한다() {
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
                .body("[0].date", is(LocalDate.now().plusDays(1).toString()))
                .body("[0].time.id", is(1))
                .body("[0].time.startAt", is("10:00"));
    }

    @Test
    void 예약_삭제시_성공하면_204를_반환한다() {
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
    void 예약_삭제시_존재하지_않는_예약이면_404를_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/api/v1/reservations/1")
                .then().log().all()
                .statusCode(404)
                .body("errorCode", is("RESERVE404_001"));
    }

    @Test
    void 예약_생성시_예약_가능한_시간이면_201을_반환한다() {
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
    void 예약_생성시_이미_예약된_시간이면_409를_반환한다() {
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
                .statusCode(409)
                .body("errorCode", is("RESERVATION409_001"));
    }

    @Test
    void 예약_생성시_date가_누락되면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        Map<String, Object> params = reservationParams();
        params.remove("date");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 예약_생성시_날짜_형식이_잘못되면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("date", "2026/05/13")))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_004"));
    }

    @Test
    void 예약_생성시_이름이_비어있으면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("name", "")))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 예약_생성시_이름이_두글자_미만이면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("name", "한")))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 예약_생성시_이름이_열자_초과면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("name", "한".repeat(11))))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 예약_생성시_이름에_특수문자가_포함되면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("name", "한+한")))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 예약_생성시_timeId가_누락되면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        Map<String, Object> params = reservationParams();
        params.remove("timeId");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 예약_생성시_timeId가_음수면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("timeId", "-2")))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 예약_생성시_themeId가_누락되면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        Map<String, Object> params = reservationParams();
        params.remove("themeId");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 예약_생성시_themeId가_음수면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("themeId", "-4")))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_001"));
    }

    @Test
    void 예약_생성시_지난_날짜이면_400을_반환한다() {
        createDefaultTimes();
        createDefaultThemes();

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of(
                        "date", LocalDate.now().minusDays(1).toString()
                )))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("RESERVATION400_001"));
    }

    @Test
    void 사용자가_자신의_이름으로_예약을_조회할때_이름이_두글자_미만이면_400을_반환한다() {
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
                .when().get("/api/v1/reservations?userName=브")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_006"));
    }

    @Test
    void 사용자가_자신의_이름으로_예약을_조회할때_이름이_열글자_초과면_400을_반환한다() {
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
                .when().get("/api/v1/reservations?userName=" + "브".repeat(11))
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_006"));
    }

    @Test
    void 사용자가_자신의_이름으로_예약을_조회할때_이름에_특수문자가_있으면_400을_반환한다() {
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
                .when().get("/api/v1/reservations?userName=브라+운")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("COMMON400_006"));
    }

    @Test
    @Sql(statements = {
            "INSERT INTO reservation_time (id, start_at) VALUES (1, '10:00')",
            "INSERT INTO theme (id, name, description, img_url) VALUES (1, '이든의 공포 하우스', '이든이 귀신으로 나옴', 'https://images.example.com/themes/horror-house.jpg')",
            "INSERT INTO reservation (id, name, date, time_id, theme_id) VALUES (1, '브라운', DATEADD('DAY', -1, CURRENT_DATE()), 1, 1)"
    })
    void 이미_지난_예약을_삭제하면_400을_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/api/v1/reservations/1")
                .then().log().all()
                .statusCode(400)
                .body("errorCode", is("RESERVATION400_002"));
    }

    @Test
    void 사용자가_본인의_예약_시간을_변경할_수_있다() {
        createDefaultThemes();
        createDefaultTimes();

        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
        params.put("timeId", 3L);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().patch("/api/v1/reservations/1")
                .then().log().all()
                .statusCode(200)
                .body("time.startAt", is("12:00"));
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
        themeParams.put("imgUrl", "https://images.example.com/themes/horror-house.jpg");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/admin/themes")
                .then().statusCode(201);

        Map<String, Object> themeParams2 = new HashMap<>();
        themeParams2.put("name", "정콩이의 방탈출");
        themeParams2.put("description", "니는 못나간다");
        themeParams2.put("imgUrl", "https://images.example.com/themes/jungkong-room.jpg");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeParams2)
                .when().post("/api/v1/admin/themes")
                .then().statusCode(201);
    }

    private Map<String, Object> reservationParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", LocalDate.now().plusDays(1).toString());
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
