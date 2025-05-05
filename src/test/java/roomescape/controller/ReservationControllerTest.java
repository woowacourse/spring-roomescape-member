package roomescape.controller;

import static org.hamcrest.CoreMatchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class ReservationControllerTest {

    private static final String NAME = "테스트";
    private static final String DATE = LocalDate.MAX.toString();
    private static final int TIME_ID = 1;
    private static final int THEME_ID = 1;

    @Test
    @DisplayName("예약 목록을 조회한다")
    void readAllReservations() {
        RestAssured.given().log().all()
                .when().get("reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약을 생성한다")
    void createReservation() {
        //given
        Map<String, Object> request = createReservationRequest();

        //when
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(request)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(2),
                        "name", is(NAME),
                        "date", is(DATE),
                        "reservationTime.id", is(TIME_ID),
                        "theme.id", is(THEME_ID)
                );
    }

    @Test
    @DisplayName("예약을 삭제한다")
    void deleteReservation() {
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    private Map<String, Object> createReservationRequest() {
        Map<String, Object> reservation = new HashMap<>();
        reservation.put("name", NAME);
        reservation.put("date", DATE);
        reservation.put("timeId", TIME_ID);
        reservation.put("themeId", THEME_ID);
        return reservation;
    }
}
