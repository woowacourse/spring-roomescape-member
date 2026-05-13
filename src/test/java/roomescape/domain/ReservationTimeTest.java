package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    @Test
    void 예약_시간을_생성할_때_시작_시간_정보가_없다면_예외가_발생한다() {
        // when & then
        assertThatThrownBy(() -> new ReservationTime(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("추가 할 예약 시작 시간 정보가 누락되었습니다");
    }

    @Test
    void 유효한_예약_시간을_생성할_수_있다() {
        // given
        LocalTime startAt = LocalTime.of(11, 0);

        // when
        ReservationTime reservationTime = new ReservationTime(startAt);

        // then
        assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
    }

    @Test
    void 예약_시간은_날짜_정보를_통해_예약_일자를_생성할_수_있다() {
        // given
        LocalDate reservationDate = LocalDate.of(2026, 1, 1);
        LocalTime startAt = LocalTime.of(10, 0);
        ReservationTime reservationTime = new ReservationTime(startAt);

        // when
        LocalDateTime reservationDateTime = reservationTime.toReservationDateTime(reservationDate);

        // then
        assertThat(reservationDateTime).isEqualTo(LocalDateTime.of(reservationDate, startAt));
    }
}
