package roomescape.integration.api;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import roomescape.common.BaseTest;

class TimeApiTest extends BaseTest {

    private Map<String, String> reservationTime;

    @BeforeEach
    void setUp() {
        reservationTime = Map.of("startAt", "10:00");
    }

    @Test
    void 예약_시간을_생성한다() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTime)
                .when().post("/times")
                .then().log().all()
                .statusCode(201)
                .body("id", is(1))
                .body("startAt", is("10:00"));
    }

    @Test
    void 예약_시간을_조회한다() {
        예약_시간을_생성한다();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", is(1))
                .body("[0].startAt", is("10:00"));
    }

    @Test
    void 예약_시간을_삭제한다() {
        예약_시간을_생성한다();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().delete("/times/{id}", 1L)
                .then().log().all()
                .statusCode(204);
    }

    @Test
    void 예약_시간들의_예약_가능_여부를_조회한다() {
        예약_시간을_생성한다();
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .when().get("/times/available")
                .then().log().all()
                .statusCode(200)
                .body("size()", greaterThan(0))
                .body("[0].id", is(1))
                .body("[0].startAt", is("10:00"))
                .body("[0].isReserved", is(false));
    }
}
