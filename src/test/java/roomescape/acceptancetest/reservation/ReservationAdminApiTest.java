package roomescape.acceptancetest.reservation;

import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;
import roomescape.acceptancetest.fixture.AcceptanceTestFixture;

@RoomecapeAcceptanceTest
class ReservationAdminApiTest {

    @Autowired
    private AcceptanceTestFixture acceptanceTestFixture;

    @Test
    @DisplayName("예약 조회")
    void findReservations_success() {
        RestAssured.given().log().all()
                .when().get("/admin/reservations")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(0));
    }

    @Test
    @DisplayName("예약 삭제")
    void deleteReservation_success() {
        acceptanceTestFixture.createTheme();
        acceptanceTestFixture.createReservationTime("15:40", 1L);
        acceptanceTestFixture.createReservation("브라운", acceptanceTestFixture.reservationDate(), 1L);

        RestAssured.given().log().all()
                .when().delete("/admin/reservations/1")
                .then().log().all()
                .statusCode(204);
    }

}
