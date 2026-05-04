package roomescape.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간 객체를 성공적으로 생성한다.")
    void create_success() {
        // given
        LocalTime startTime = LocalTime.of(10, 0);

        // when
        ReservationTime reservationTime = new ReservationTime(1L, startTime);

        // then
        assertThat(reservationTime.getId()).isEqualTo(1L);
        assertThat(reservationTime.getStartTime()).isEqualTo(startTime);
    }

    @Test
    @DisplayName("시작 시간이 null이면 객체 생성 시 예외가 발생한다.")
    void create_fail_null_time() {
        // given
        LocalTime startTime = null;

        // when & then
        assertThatThrownBy(() -> new ReservationTime(1L, startTime))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시작 시간은 필수입니다.");
    }

    @Test
    @DisplayName("ID가 없어도 시작 시간이 있으면 객체 생성에 성공한다.")
    void create_success_without_id() {
        // given
        LocalTime startTime = LocalTime.of(13, 0);

        // when
        ReservationTime reservationTime = new ReservationTime(startTime);

        // then
        assertThat(reservationTime.getId()).isNull();
        assertThat(reservationTime.getStartTime()).isEqualTo(startTime);
    }
}
