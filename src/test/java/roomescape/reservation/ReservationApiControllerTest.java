package roomescape.reservation;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import roomescape.TestSupport;

class ReservationApiControllerTest extends TestSupport {

    @Test
    @DisplayName("예약 목록을 조회한다.")
    void findAllTest() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @TestFactory
    @DisplayName("예약을 추가하고 삭제합니다.")
    Collection<DynamicTest> createAndDeleteReservation() {

        Map<String, Object> timeParams = Map.of(
                "startAt", "10:00"
        );

        Map<String, Object> params = Map.of(
                "name", "브라운",
                "date", "2023-08-05",
                "timeId", 1L);

        return List.of(
                DynamicTest.dynamicTest("시간을 추가한다", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(timeParams)
                            .when().post("/times")
                            .then().log().all()
                            .statusCode(200);
                }),

                DynamicTest.dynamicTest("예약을 추가한다.", () -> {
                    RestAssured.given().log().all()
                            .contentType(ContentType.JSON)
                            .body(params)
                            .when().post("/reservations")
                            .then().log().all()
                            .statusCode(200)
                            .body("id", is(1));
                }),

                DynamicTest.dynamicTest("예약을 조회했을 때 하나이다.", () -> {
                    RestAssured.given().log().all()
                            .when().get("/reservations")
                            .then().log().all()
                            .statusCode(200)
                            .body("size()", is(1));
                }),

                DynamicTest.dynamicTest("예약을 조회했을 때 브라운 이름이 존재한다.", () -> {
                    RestAssured.given().log().all()
                            .when().get("/reservations")
                            .then().log().all()
                            .statusCode(200)
                            .body("name", contains("브라운"));
                }),

                DynamicTest.dynamicTest("예약을 삭제한다.", () -> {
                    RestAssured.given().log().all()
                            .when().delete("/reservations/1")
                            .then().log().all()
                            .statusCode(200);
                }),

                DynamicTest.dynamicTest("예약을 삭제한 후 조회 했을 때 갯수가 0이다.", () -> {
                    RestAssured.given().log().all()
                            .when().get("/reservations")
                            .then().log().all()
                            .statusCode(200)
                            .body("size()", is(0));
                })
        );
    }
}
