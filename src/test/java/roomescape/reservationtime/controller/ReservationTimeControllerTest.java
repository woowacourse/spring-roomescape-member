package roomescape.reservationtime.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ReservationTimeControllerTest {

    @DisplayName("특정 날짜/테마의 예약 가능 시간대 조회 API를 테스트합니다.")
    @Test
    void find_available_times() {
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .queryParam("date", "2026-05-04")
                .queryParam("themeId", "1")
                .when().get("/times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(10))
                .body("startAt", containsInAnyOrder("09:00", "10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00", "17:00", "18:00"));
    }
}
