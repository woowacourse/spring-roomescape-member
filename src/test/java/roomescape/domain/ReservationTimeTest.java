package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.global.exception.reservationtime.InvalidReservationTimeException;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간을 정상적으로 생성한다.")
    void createReservationTime() {
        LocalTime time = LocalTime.of(13, 0);
        ReservationTime reservationTime = ReservationTime.createNew(time);

        assertThat(reservationTime.getStartAt()).isEqualTo(time);
        assertThat(reservationTime.getId()).isNull();
    }

    @Test
    @DisplayName("예약 시간이 null인 경우 예외가 발생한다.")
    void validateNotNull() {
        assertThatThrownBy(() -> ReservationTime.createNew(null))
                .isInstanceOf(InvalidReservationTimeException.class)
                .hasMessage("예약 시간은 비어있을 수 없습니다.");
    }
}
