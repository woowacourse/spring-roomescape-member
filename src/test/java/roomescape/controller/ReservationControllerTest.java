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

    /*
    발생할 수 있는 예외 상황에 대한 처리를 하여, 사용자에게 적절한 응답을 합니다.
- [ ] 예약 생성 시 예약자명, 날짜, 시간에 유효하지 않은 값이 입력 되었을 때
- [ ] 특정 시간에 대한 예약이 존재하는데, 그 시간을 삭제하려 할 때
- [ ] 시간 생성 시 시작 시간에 유효하지 않은 값이 입력되었을 때
     */

    @DisplayName("예약 생성 시 유효하지 않은 이름이 입력 되었을 때")
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
            .body(is("예약 정보를 잘못 입력했습니다."));
    }

    @DisplayName("예약 생성 시 유효하지 않은 시간이 입력 되었을 때")
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
            .body(is("예약 정보를 잘못 입력했습니다."));

    }

    private static void addOneTimeSlot() {
        Map<String, String> params = new HashMap<>();
        params.put("startAt", "10:00");

        RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(params)
            .when().post("/times")
            .then().log().all()
            .statusCode(HttpStatus.CREATED.value());
    }
}
