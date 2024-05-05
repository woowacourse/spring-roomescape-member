package roomescape.controller.reservation;

import static org.hamcrest.Matchers.is;
import static roomescape.InitialDataFixture.INITIAL_RESERVATION_COUNT;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Sql("/initial_test_data.sql")
class ReservationControllerTest {

    @Test
    @DisplayName("저장된 reservation을 모두 반환한다.")
    void getReservations() {
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(INITIAL_RESERVATION_COUNT));
    }

    @Test
    @DisplayName("Reservation을 추가한다.")
    void addReservation() {
        //given
        LocalDate localDate = LocalDate.now().plusDays(1);
        Map<String, String> reservationParams = new HashMap<>();
        reservationParams.put("name", "브리");
        reservationParams.put("date", localDate.toString());
        reservationParams.put("timeId", "1");
        reservationParams.put("themeId", "1");

        //when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(reservationParams)
                .when().post("/reservations")
                .then().log().all()
                .statusCode(201)
                .body("id", is(INITIAL_RESERVATION_COUNT + 1))
                .header("Location", String.format("/reservations/%d", INITIAL_RESERVATION_COUNT + 1));
    }

    @Test
    @DisplayName("Reservation을 삭제한다.")
    void deleteReservation() {
        //when
        RestAssured.given().log().all()
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        //then
        RestAssured.given().log().all()
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(INITIAL_RESERVATION_COUNT - 1));
    }
}
