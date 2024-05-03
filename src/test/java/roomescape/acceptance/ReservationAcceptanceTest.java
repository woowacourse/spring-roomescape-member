package roomescape.acceptance;

import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.test.context.jdbc.Sql;

@Sql("/init-for-reservation.sql")
class ReservationAcceptanceTest extends BasicAcceptanceTest {
    @TestFactory
    @DisplayName("예약을 추가한다")
    Stream<DynamicTest> reservationTest() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        return Stream.of(
                dynamicTest("예약을 추가한다", () -> ReservationCRD.postReservation(tomorrow.toString(), 1L, 1L, 201))
        );
    }

    @TestFactory
    @DisplayName("과거 시간에 대한 예약을 하면, 예외가 발생한다")
    Stream<DynamicTest> pastTimeReservationTest() {
        LocalDate yesterday = LocalDate.now().minusDays(1);

        return Stream.of(
                dynamicTest("과거 시간에 대한 예약을 추가한다", () -> ReservationCRD.postReservation(yesterday.toString(), 1L, 1L, 400))
        );
    }

    @TestFactory
    @DisplayName("이미 예약된 테마와 시간을 예약을 하면, 예외가 발생한다")
    Stream<DynamicTest> duplicateReservationTest() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        return Stream.of(
                dynamicTest("예약을 추가한다", () -> ReservationCRD.postReservation(tomorrow.toString(), 1L, 1L, 201)),
                dynamicTest("동일한 예약을 추가한다", () -> ReservationCRD.postReservation(tomorrow.toString(), 1L, 1L, 400)),
                dynamicTest("다른 테마를 예약한다", () -> ReservationCRD.postReservation(tomorrow.toString(), 1L, 2L, 201))
        );
    }
}

