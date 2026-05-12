package roomescape.acceptancetest.reservationtime;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;

import io.restassured.RestAssured;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import roomescape.acceptancetest.RoomecapeAcceptanceTest;
import roomescape.acceptancetest.fixture.AcceptanceTestFixture;

@RoomecapeAcceptanceTest
public class ReservationTimeApiTest {

    @Autowired
    private AcceptanceTestFixture acceptanceTestFixture;

    @Test
    void 예약_가능_시간_API() {
        acceptanceTestFixture.createTheme("미술관의 밤", "aa", "aa");
        acceptanceTestFixture.createReservationTime("10:00", 1L);
        acceptanceTestFixture.createReservationTime("11:00", 1L);

        String date = acceptanceTestFixture.reservationDate().toString();
        acceptanceTestFixture.createReservation("브라운", LocalDate.parse(date), 1L);

        RestAssured.given().log().all()
                .queryParam("date", date)
                .when().get("/themes/1/available-times")
                .then().log().all()
                .statusCode(200)
                .body("size()", is(1))
                .body("id", contains(2))
                .body("startAt", contains("11:00:00"));
    }

}
