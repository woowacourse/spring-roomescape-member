package roomescape.reservation.domain;

import java.time.LocalDate;
import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import roomescape.reservationTime.domain.ReservationTime;

class ReservationTest {

    @DisplayName("예약자 이름에 빈 값 혹은 null이 올 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void reservation_name_is_not_blank(final String name) {
        Assertions.assertThatThrownBy(() -> new Reservation(
                null,
                name,
                LocalDate.of(2020, 10, 1),
                new ReservationTime(null, LocalTime.of(13, 0)))
        );
    }
}
