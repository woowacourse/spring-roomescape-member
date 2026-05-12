package roomescape.acceptancetest.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;
import roomescape.acceptancetest.fixture.AcceptanceTestFixture;
import roomescape.reservation.controller.dto.ReservationUpdateRequest;

@RoomecapeAcceptanceTest
public class ReservationApiTest {

    @Autowired
    private AcceptanceTestFixture acceptanceTestFixture;


    @Test
    @DisplayName("예약 이름 기반 조회")
    void findReservationsByName_success() {
        // given
        acceptanceTestFixture.createTheme();
        acceptanceTestFixture.createReservationTime("15:40", 1L);
        acceptanceTestFixture.createReservationTime("15:50", 1L);
        acceptanceTestFixture.createReservationTime("17:50", 1L);

        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.reservationDate(), 1L);
        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.reservationDate(), 2L);
        acceptanceTestFixture.createReservation("코니", acceptanceTestFixture.reservationDate(), 3L);

        String encodedName = URLEncoder.encode("브라운", StandardCharsets.UTF_8);

        // when & then
        RestAssured.given().log().all()
                .header("Authorization", encodedName)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(2));
    }

    @Test
    @DisplayName("예약 이름 기반 삭제")
    void deleteReservationsByName_success() {
        // given
        acceptanceTestFixture.createTheme();
        acceptanceTestFixture.createReservationTime("15:40", 1L);
        acceptanceTestFixture.createReservationTime("15:50", 1L);

        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.reservationDate(), 1L);
        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.reservationDate(), 2L);

        String encodedName = URLEncoder.encode("브라운", StandardCharsets.UTF_8);

        // when
        RestAssured.given().log().all()
                .header("Authorization", encodedName)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(204);

        // then
        RestAssured.given().log().all()
                .header("Authorization", encodedName)
                .when().get("/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1));
    }

    @Test
    @DisplayName("예약 이름 기반 삭제 이름이 같지 않으면 삭제 실패 예외")
    void deleteReservationsByName_nameNotMatch_exception() {
        // given
        acceptanceTestFixture.createTheme();
        acceptanceTestFixture.createReservationTime("15:40", 1L);
        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.reservationDate().minusDays(1), 1L);

        String encodedName = URLEncoder.encode("쿠다", StandardCharsets.UTF_8);

        // when & then
        RestAssured.given().log().all()
                .header("Authorization", encodedName)
                .when().delete("/reservations/1")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 이름 기반 수정")
    void updateReservationsByName_success() {
        // given
        acceptanceTestFixture.createTheme();
        acceptanceTestFixture.createReservationTime("15:40", 1L);
        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.reservationDate(), 1L);

        String encodedName = URLEncoder.encode("브라운", StandardCharsets.UTF_8);
        LocalDate newDate = acceptanceTestFixture.reservationDate().plusDays(7);
        ReservationUpdateRequest request = new ReservationUpdateRequest(newDate, 1L);
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", encodedName)
                .body(request)
                .when().patch("/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

    @Test
    @DisplayName("예약 이름 기반 수정 과거 날짜로 수정 실패 예외")
    void updateReservationsByName_pastDate_exception() {
        // given
        acceptanceTestFixture.createTheme();
        acceptanceTestFixture.createReservationTime("15:40", 1L);
        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.reservationDate(), 1L);

        String encodedName = URLEncoder.encode("브라운", StandardCharsets.UTF_8);
        LocalDate newDate = acceptanceTestFixture.reservationDate().minusDays(2);
        ReservationUpdateRequest request = new ReservationUpdateRequest(newDate, 1L);
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", encodedName)
                .body(request)
                .when().patch("/reservations/1")
                .then().log().all()
                .statusCode(400);
    }

    @Test
    @DisplayName("예약 이름 기반 수정 이미 있는 예약 날짜 예외")
    void updateReservationsByName_alreadyExistDate_exception() {
        // given
        acceptanceTestFixture.createTheme();
        acceptanceTestFixture.createReservationTime("15:40", 1L);
        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.reservationDate(), 1L);
        acceptanceTestFixture.createReservation("쿠다", acceptanceTestFixture.reservationDate().plusDays(1), 1L);

        String encodedName = URLEncoder.encode("브라운", StandardCharsets.UTF_8);
        LocalDate newDate = acceptanceTestFixture.reservationDate().plusDays(1);
        ReservationUpdateRequest request = new ReservationUpdateRequest(newDate, 1L);
        // when & then
        RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .header("Authorization", encodedName)
                .body(request)
                .when().patch("/reservations/1")
                .then().log().all()
                .statusCode(409);
    }

}
