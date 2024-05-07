package roomescape.reservation.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.model.RoomEscapeException;
import roomescape.reservation.exception.ReservationExceptionCode;
import roomescape.theme.domain.Theme;
import roomescape.time.domain.Time;

class ReservationTest {

    private static final LocalTime TIME = LocalTime.of(9, 0);
    private static final LocalDate TODAY = LocalDate.now();

    @Test
    @DisplayName("전달 받은 데이터로 Reservation 객체를 정상적으로 생성한다.")
    void constructReservation() {
        Theme theme = new Theme(1, "미르", "미르 방탈출", "썸네일 Url");
        Time time = new Time(1, TIME);
        Reservation reservation = Reservation.reservationOf(1L, "폴라", TODAY, time, theme);

        assertAll(
                () -> assertEquals(reservation.getTheme(), theme),
                () -> assertEquals(reservation.getReservationTime(), time),
                () -> assertEquals(reservation.getId(), 1),
                () -> assertEquals(reservation.getName(), "폴라"),
                () -> assertEquals(reservation.getDate(), TODAY)
        );
    }

    @Test
    @DisplayName("전달된 id와 같은 값의 id인지 확인.")
    void hasSameId() {
        Reservation reservation = Reservation.reservationOf(1L, "폴라", TODAY, new Time(TIME),
                new Theme("polla", "폴라 방탈출", "thumbnail"));

        assertAll(
                () -> Assertions.assertThat(reservation.hasSameId(2L)).isFalse(),
                () -> Assertions.assertThat(reservation.hasSameId(1L)).isTrue()
        );
    }

    @Test
    @DisplayName("이름이 Null 인 경우 에러를 발생한다.")
    void validation_ShouldThrowException_WhenNameIsNull() {
        Throwable nameIsNull = assertThrows(RoomEscapeException.class,
                () -> Reservation.saveReservationOf(null, TODAY, 1L, 1L));
        assertEquals(ReservationExceptionCode.NAME_IS_NULL_OR_BLANK_EXCEPTION.getMessage(), nameIsNull.getMessage());
    }

    @Test
    @DisplayName("이름이 빈값인 경우 에러를 발생한다.")
    void validation_ShouldThrowException_WhenNameIsEmpty() {
        Throwable nameIsEmpty = assertThrows(
                RoomEscapeException.class, () -> Reservation.saveReservationOf(" ", TODAY, 1L, 1L));

        assertEquals(ReservationExceptionCode.NAME_IS_NULL_OR_BLANK_EXCEPTION.getMessage(), nameIsEmpty.getMessage());
    }

    @Test
    @DisplayName("과거의 날짜를 예약하려고 시도하는 경우 에러를 발생한다.")
    void validation_ShouldThrowException_WhenReservationDateIsPast() {
        Throwable pastDateReservation = assertThrows(RoomEscapeException.class,
                () -> Reservation.saveReservationOf("pollari", TODAY.minusDays(1), 1L, 1L));

        assertEquals(ReservationExceptionCode.RESERVATION_DATE_IS_PAST_EXCEPTION.getMessage(),
                pastDateReservation.getMessage());
    }

    @Test
    @DisplayName("이름에 특수문자가 들어가는 경우 에러를 발생한다.")
    void validation_ShouldThrowException_WhenNameContainsSymbol() {
        Throwable pastDateReservation = assertThrows(RoomEscapeException.class,
                () -> Reservation.saveReservationOf("@특수문자", TODAY, 1L, 1L));

        assertEquals(ReservationExceptionCode.ILLEGAL_NAME_FORM_EXCEPTION.getMessage(),
                pastDateReservation.getMessage());
    }
}
