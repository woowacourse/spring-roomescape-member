package roomescape.acceptance;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import roomescape.service.dto.request.ReservationRequest;
import roomescape.service.dto.request.ReservationTimeRequest;
import roomescape.service.dto.request.ThemeRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ReservationTimeTest extends AcceptanceTest{

    @BeforeEach
    void insert() {
        ReservationTimeRequest reservationTimeRequest = new ReservationTimeRequest(LocalTime.of(10, 0));
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .post("/times");

        ThemeRequest themeRequest = new ThemeRequest("hi", "happy", "abcd.html");
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(themeRequest)
                .post("/themes");

        ReservationRequest reservationRequest = new ReservationRequest("rush", LocalDate.of(2030, 12, 12), 1L, 1L);
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationRequest)
                .post("/reservations");
    }

    @DisplayName("올바르지 않은 시간 형식으로 입력시 예외처리")
    @Test
    void invalidTimeFormat() {
        Map<String, String> reservationTimeRequest = new HashMap<>();
        reservationTimeRequest.put("startAt", "aaa11");

        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationTimeRequest)
                .when().post("/times")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("예약되어 있는 시간 삭제시 예외처리")
    @Test
    void deleteTimeInUse() {
        RestAssured.given().log().all()
                .when().delete("/times/1")
                .then().log().all()
                .statusCode(400);
    }

    @DisplayName("테마와 날짜 정보를 주면 시간별 예약가능 여부를 반환한다.")
    @Test
    void availableTime() {
        RestAssured.given().log().all()
                .when().get("/times/availability?themeId=1&date=2999-12-12")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }
}
