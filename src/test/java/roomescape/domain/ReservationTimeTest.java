package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("비어있는 ID값으로 예약시간을 생성할 수 없다")
    @Test
    void cannotCreateBecauseNullId() {
        Long nullId = null;
        assertThatThrownBy(() -> new ReservationTime(nullId, LocalTime.now()));
    }

    @DisplayName("비어있는 예약 시간으로 예약시간을 생성할 수 없다")
    @Test
    void cannotCreateBecauseNullStartAt() {
        LocalTime nullStartAt = null;
        assertThatThrownBy(() -> new ReservationTime(1L, nullStartAt));
    }

    @DisplayName("나노초 단위는 예약 시간으로 다루지 않는다")
    @Test
    void canNotHandleNanoSecond() {
        LocalTime startAt = LocalTime.of(10, 5, 30, 10);
        ReservationTime reservationTime = new ReservationTime(1L, startAt);

        assertThat(reservationTime.getStartAt().getNano()).isEqualTo(0);
    }
}
