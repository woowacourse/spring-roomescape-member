package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.is;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    private Map<String, Object> reservationParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", 1);
        params.put("themeId", 1);
        return params;
    }

    @BeforeEach
    void setUp() {
        Map<String, String> time = new HashMap<>();
        time.put("startAt", "10:00");

        RestAssured.given().contentType(ContentType.JSON)
                .body(time)
                .when().post("/api/v1/times");

        Map<String, Object> themeParams = new HashMap<>();
        themeParams.put("name", "이든의 공포 하우스");
        themeParams.put("description", "이든이 귀신으로 나옴");
        themeParams.put("imgUrl", "링크~");
        themeParams.put("userName", "ADMIN");
        RestAssured.given().contentType(ContentType.JSON)
                .body(themeParams)
                .when().post("/api/v1/themes");
    }

    @Test
    void 예약_조회() {
        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    void 예약_추가() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1));
    }

    @Test
    void 예약_추가_조회() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations");

        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("[0].id", is(1))
                .body("[0].name", is("브라운"))
                .body("[0].date", is("2023-08-05"))
                .body("[0].time.id", is(1))
                .body("[0].time.startAt", is("10:00"));
    }

    @Test
    void 예약_추가_및_삭제() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations");

        RestAssured.given().log().all()
                .when().get("/api/v1/reservations");

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
    void 존재하지_않는_예약을_삭제하면_404를_반환한다() {
        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));

        RestAssured.given().log().all()
                .when().delete("/api/v1/reservations/1")
                .then().log().all()
                .statusCode(404);
    }

    @Test
    void 예약과_시간_연결() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams())
                .when().post("/api/v1/reservations");

        RestAssured.given().log().all()
                .when().get("/api/v1/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
