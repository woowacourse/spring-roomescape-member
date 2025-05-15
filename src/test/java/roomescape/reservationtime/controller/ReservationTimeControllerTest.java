package roomescape.reservationtime.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationTimeControllerTest {
    @Test
    void 예약시간_추가_테스트() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        Long id = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(params)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .extract().as(Long.class);

        assertThat(id).isEqualTo(9L);
    }

    @Test
    void 예약시간_조회_테스트() {
        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(8));
    }

    @Test
    void 예약시간_삭제_테스트() {
        RestAssured.given().log().all()
                .when().delete("/times/4")
                .then().log().all()
                .statusCode(204);

        RestAssured.given().log().all()
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(7));
    }

    @Test
    void 가능한_예약시간_조회_테스트() {
        RestAssured.given().log().all()
                .when().get("/times/1?date=2025-01-01")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(8));
    }
}
