package roomescape.reservation.domain;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.time.domain.Time;
import roomescape.exception.BadRequestException;

class ReservationTest {
    @Test
    @DisplayName("전달된 id와 같은 값의 id인지 확인.")
    void hasSameId() {
        Reservation reservation = new Reservation(1L, "폴라", LocalDate.now(), new Time(LocalTime.now()));

        assertAll(() -> Assertions.assertThat(reservation.hasSameId(2L))
                .isFalse(), () -> Assertions.assertThat(reservation.hasSameId(1L))
                .isTrue());
    }

    @Test
    @DisplayName("이름이 Null 혹은 빈값인 경우 에러를 발생한다.")
    void validation_ShouldThrowException_WhenNameIsNull() {
        assertAll(() -> {
            Throwable nameIsEmpty = assertThrows(BadRequestException.class, () -> new Reservation(" ", LocalDate.now(), 1L));
            assertEquals("null 혹은 빈칸으로 이루어진 이름으로 예약을 시도하였습니다.", nameIsEmpty.getMessage());

            Throwable nameIsNull = assertThrows(BadRequestException.class, () -> new Reservation(null, LocalDate.now(), 1L));
            assertEquals("null 혹은 빈칸으로 이루어진 이름으로 예약을 시도하였습니다.", nameIsNull.getMessage());
            }
        );
    }

    @Test
    @DisplayName("과거의 날짜를 예약하려고 시도하는 경우 에러를 발생한다.")
    void validation_ShouldThrowException_WhenReservationDateIsPast() {
        assertAll(() -> {
            Throwable pastDateReservation = assertThrows(BadRequestException.class, () -> new Reservation(" ", LocalDate.now().minusDays(1), 1L));
            assertEquals("null 혹은 빈칸으로 이루어진 이름으로 예약을 시도하였습니다.", pastDateReservation.getMessage());
            }
        );
    }

    @Test
    @DisplayName("이름에 특수문자가 들어가는 경우 에러를 발생한다.")
    void validation_ShouldThrowException_WhenNameContainsSymbol() {
        assertAll(() -> {
                    Throwable pastDateReservation = assertThrows(BadRequestException.class, () -> new Reservation("@특수문자", LocalDate.now(), 1L));
                    assertEquals("특수문자가 포함된 이름으로 예약을 시도하였습니다.", pastDateReservation.getMessage());
                }
        );
    }
}
