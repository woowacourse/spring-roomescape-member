package roomescape.reservation.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.reservation.request.ReservationRequest;
import roomescape.time.domain.ReservationTime;

class ReservationDateTimeTest {

    @Test
    @DisplayName("기준 시점이 입력 시점보다 과거라면 예외가 발생한다.")
    void validatePastTest() {
        LocalDate date = LocalDate.parse("2023-04-23");
        LocalTime time = LocalTime.parse("15:30");
        ReservationRequest request = new ReservationRequest(date, 0, 0,0);
        ReservationTime reservationTime = new ReservationTime(time);
        ReservationDateTime reservationDateTime = new ReservationDateTime(request, reservationTime);

        assertAll(
                () -> assertThatThrownBy(() -> reservationDateTime.validatePast(LocalDateTime.of(date, time.plusHours(1))))
                        .isInstanceOf(IllegalArgumentException.class),
                () -> assertThatCode(() -> reservationDateTime.validatePast(LocalDateTime.of(date, time).minusHours(1)))
                        .doesNotThrowAnyException()
        );
    }
}
