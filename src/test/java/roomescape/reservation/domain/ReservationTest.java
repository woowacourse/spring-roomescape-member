package roomescape.reservation.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.model.RoomEscapeException;
import roomescape.reservation.exception.ReservationExceptionCode;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

class ReservationTest {

    private static final LocalTime TIME = LocalTime.of(9, 0);
    private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);
    private static final LocalDate BEFORE = LocalDate.now().minusDays(1);

    @Test
    @DisplayName("전달 받은 데이터로 Reservation 객체를 정상적으로 생성한다.")
    void constructReservation() {
        Theme theme = new Theme(1, "미르", "미르 방탈출", "썸네일 Url");
        Time time = new Time(1, TIME);
        Reservation reservation = Reservation.reservationOf(1L, "폴라", TOMORROW, time, theme);

        assertAll(
                () -> assertEquals(reservation.getTheme(), theme),
                () -> assertEquals(reservation.getReservationTime(), time),
                () -> assertEquals(reservation.getId(), 1),
                () -> assertEquals(reservation.getName(), "폴라"),
                () -> assertEquals(reservation.getDate(), TOMORROW)
        );
    }

    @Test
    @DisplayName("과거의 날짜를 예약하려고 시도하는 경우 에러를 발생한다.")
    void validation_ShouldThrowException_WhenReservationDateIsPast() {
        Throwable pastDateReservation = assertThrows(RoomEscapeException.class,
                () -> Reservation.saveReservationOf("pollari", BEFORE, 1L, 1L));

        assertEquals(ReservationExceptionCode.RESERVATION_DATE_IS_PAST_EXCEPTION.getMessage(),
                pastDateReservation.getMessage());
    }
}
