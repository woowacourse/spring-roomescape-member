package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import roomescape.exception.reservation.ReservationFieldRequiredException;

class ReservationTest {
    @DisplayName("name이 빈 값이거나 null이면 예외가 발생한다")
    @ParameterizedTest
    @NullAndEmptySource
    void validateName(String name) {
        // given
        LocalDate date = LocalDate.now();
        ReservationTime time = new ReservationTime(LocalTime.now());

        // when // then
        assertThatThrownBy(() -> new Reservation(name, date, time))
                .isInstanceOf(ReservationFieldRequiredException.class);
    }

    @DisplayName("date이 null이면 예외가 발생한다")
    @Test
    void validateDate() {
        // given
        String name = "에드";
        LocalDate date = null;
        ReservationTime time = new ReservationTime(LocalTime.now());

        // when // then
        assertThatThrownBy(() -> new Reservation(name, date, time))
                .isInstanceOf(ReservationFieldRequiredException.class);
    }

    @DisplayName("time이 null이면 예외가 발생한다")
    @Test
    void validateTime() {
        // given
        String name = "에드";
        LocalDate date = LocalDate.now().plusDays(1);
        ReservationTime time = null;

        // when // then
        assertThatThrownBy(() -> new Reservation(name, date, time))
                .isInstanceOf(ReservationFieldRequiredException.class);
    }
}
