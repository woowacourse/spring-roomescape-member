package roomescape.reservation.controller;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/sql/data.sql")
public class ReservationControllerTest {

    @Test
    void 예약_추가_테스트() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", "브라운");
        reservation.put("date", "2026-08-05");
        reservation.put("timeId", 1);
        reservation.put("themeId", 1);

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservation)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("name", equalTo("브라운"));

    }

    @Test
    void 예약_조회_테스트() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(4));
    }

    @Test
    void 에약_삭제_테스트() {
        RestAssured.given().log().all()
                .when().delete("/reservations/3")
                .then().log().all()
                .statusCode(204);
    }
}
