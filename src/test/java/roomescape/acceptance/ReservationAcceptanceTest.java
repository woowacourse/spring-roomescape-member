package roomescape.acceptance;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

public class ReservationAcceptanceTest extends BasicAcceptanceTest {
    @DisplayName("어드민이 예약을 추가한다")
    @Sql("/test-data/reservation.sql")
    @TestFactory
    Stream<DynamicTest> reservationTest() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        return Stream.of(
                dynamicTest("예약을 추가한다", () -> postReservation(tomorrow.toString(), 1L, 1L, 201))
        );
    }

    @DisplayName("과거 시간에 대한 예약을 하면, 예외가 발생한다")
    @Sql("/test-data/reservation.sql")
    @TestFactory
    Stream<DynamicTest> pastTimeReservationTest() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        return Stream.of(
                dynamicTest("과거 시간에 대한 예약을 추가한다", () -> postReservation(yesterday.toString(), 1L, 1L, 400))
        );
    }

    @DisplayName("이미 예약된 테마와 시간을 예약을 하면, 예외가 발생한다")
    @Sql("/test-data/reservation.sql")
    @TestFactory
    Stream<DynamicTest> duplicateReservationTest() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        return Stream.of(
                dynamicTest("예약을 추가한다", () -> postReservation(tomorrow.toString(), 1L, 1L, 201)),
                dynamicTest("동일한 예약을 추가한다", () -> postReservation(tomorrow.toString(), 1L, 1L, 400)),
                dynamicTest("다른 테마를 예약한다", () -> postReservation(tomorrow.toString(), 1L, 2L, 201))
        );
    }

    private long postReservation(String date, Long timeId, Long themeId, int expectedHttpCode) {
        Map<?, ?> requestBody = Map.of("name", "포비", "date", date, "timeId", timeId, "themeId", themeId);

        Response response = RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when().post("/admin/reservations")
                .then().log().all()
                .statusCode(expectedHttpCode)
                .extract().response();

        if (expectedHttpCode == 201) {
            return response.jsonPath().getLong("id");
        }

        return 0L;
    }
}
