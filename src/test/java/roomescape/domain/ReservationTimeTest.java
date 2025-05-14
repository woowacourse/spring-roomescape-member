package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @DisplayName("비어있는 예약 시간으로 예약시간을 생성할 수 없다")
    @Test
    void cannotCreateBecauseNullStartAt() {
        // given
        LocalTime nullStartAt = null;

        // when & then
        assertThatThrownBy(() -> new ReservationTime(1L, nullStartAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 비어있는 시작시간으로 예약 시간을 생성할 수 없습니다.");
    }

    @DisplayName("나노초 단위는 예약 시간으로 다루지 않는다")
    @Test
    void canNotHandleNanoSecond() {
        // given
        LocalTime startAt = LocalTime.of(10, 5, 30, 10);
        ReservationTime reservationTime = new ReservationTime(1L, startAt);

        // when & then
        assertThat(reservationTime.getStartAt().getNano()).isEqualTo(0);
    }
}
