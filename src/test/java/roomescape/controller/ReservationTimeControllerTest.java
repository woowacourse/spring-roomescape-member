package roomescape.controller;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {

    private Map<String, Object> reservationParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2026-05-05");
        params.put("timeId", 1);
        params.put("themeId", 1);
        return params;
    }

    private Map<String, Object> reservationParams(Map<String, Object> overrides) {
        Map<String, Object> params = reservationParams();
        params.putAll(overrides);
        return params;
    }

    private void createTime(String startAt) {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", startAt);

        RestAssured.given().contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/times");
    }

    private void createTheme(String name, String description) {
        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", name);
        themeParams.put("description", description);
        themeParams.put("imgUrl", "링크~");
        themeParams.put("userName", "ADMIN");

        RestAssured.given().contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/themes");
    }

    @BeforeEach
    void setUp() {
        createTime("10:00");
        createTime("11:00");
        createTime("12:00");

        createTheme("이든의 공포 하우스", "이든이 귀신으로 나옴");
        createTheme("정콩이의 방탈출", "니는 못나간다");
    }

    @Test
    void 시간_추가() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "13:00");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/times")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/v1/times/4")
                .body("id", is(4));
    }

    @Test
    void 시간_관리_API() {
        RestAssured.given().log().all()
                .when().get("/api/v1/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3))
                .body("[0].startAt", is("10:00"))
                .body("[1].startAt", is("11:00"))
                .body("[2].startAt", is("12:00"));

        RestAssured.given().log().all()
                .when().delete("/api/v1/times/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 예약_가능_시간_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations");

        RestAssured.given().log().all()
                .when().get("/api/v1/times?date=2026-05-05&themeId=1")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(3))
                .body("[0].available", is(false))
                .body("[1].available", is(true))
                .body("[2].available", is(true));
    }

    @Test
    void 사용자는_예약_가능한_시간을_선택하여_예약_가능하다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("timeId", 2)))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/v1/reservations/2");
    }

    @Test
    void 사용자가_예약된_시간을_선택하여_예약할경우_409를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("timeId", 1)))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(409);
    }


    @Test
    void 사용자는_같은_날짜_시간이라도_테마가_다르면_각각_예약_가능하다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams(Map.of("themeId", 2)))
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .header("Location", "/api/v1/reservations/2");
    }

    @Test
    void 존재하지_않는_예약시간을_삭제하면_404를_반환한다() {
        RestAssured.given().log().all()
                .when().delete("/api/v1/times/4")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 예약이_존재하는_예약시간을_삭제하면_409를_반환한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations");

        RestAssured.given().log().all()
                .when().delete("/api/v1/times/1")
                .then().log().all()
                .statusCode(409);

    }
}
