package roomescape.controller;


import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    private static void addOneReservation() {
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", "1");

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(params)
            .post("/reservations");
    }

    private static void addOneTimeSlot() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given()
            .contentType(ContentType.JSON)
            .body(params)
            .post("/times");
    }

    @DisplayName("예약 생성 시 유효하지 않은 이름이 입력 되었을 때 Bad Request")
    @Test
    void createReservationInvalidName() {
        // given
        addOneTimeSlot();

        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", "1");

        // when & then
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body(is("이름은 5자를 넘길 수 없습니다."));
    }

    @DisplayName("예약 생성 시 유효하지 않은 시간이 입력 되었을 때 Bad Request")
    @Test
    void createReservationInvalidTimeSlot() {
        // given
        Map<String, String> params = new HashMap<>();
        params.put("name", "브라운");
        params.put("date", "2023-08-05");
        params.put("timeId", "1");

        // when & then
        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/reservations")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body(is("존재하지 않는 예약 시간입니다."));
    }

    @DisplayName("특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때 Bad Request")
    @Test
    void deleteTimeSlotWithReservation() {
        // given
        addOneTimeSlot();
        addOneReservation();

        // when & then
        RestAssured.given().log().all()
            .when().delete("/times/1")
            .then().log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value());
    }
}
