package roomescape.domain.reservationtime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import roomescape.support.exception.RoomescapeException;

class ReservationTimeTest {

    @Test
    void id가_없는_예약_시간을_생성한다() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);

        // when
        ReservationTime reservationTime = ReservationTime.createWithoutId(startAt);

        // then
        assertSoftly(softly -> {
            softly.assertThat(reservationTime.getId()).isNull();
            softly.assertThat(reservationTime.getStartAt()).isEqualTo(startAt);
        });
    }

    @Test
    void 예약_시간이_null이면_예외가_발생한다() {
        // given
        LocalTime startAt = null;

        // when & then
        assertThatThrownBy(() -> ReservationTime.createWithoutId(startAt))
            .isInstanceOf(RoomescapeException.class)
            .hasMessage("시간은 필수입니다.");
    }
}
