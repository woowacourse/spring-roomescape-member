package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import roomescape.exception.DomainViolationException;

class ReservationTimeTest {

    @Test
    void 정각으로_생성한다() {
        ReservationTime time = new ReservationTime(LocalTime.of(13, 0));
        assertThat(time.getStartAt()).isEqualTo(LocalTime.of(13, 0));
    }

    @Test
    void 반시간으로_생성한다() {
        ReservationTime time = new ReservationTime(LocalTime.of(9, 30));
        assertThat(time.getStartAt()).isEqualTo(LocalTime.of(9, 30));
    }

    @ParameterizedTest
    @MethodSource("invalidMinutes")
    void 정각_또는_30분이_아니면_예외가_발생한다(int minute) {
        assertThatThrownBy(() -> new ReservationTime(LocalTime.of(12, minute)))
            .isInstanceOf(DomainViolationException.class);
    }

    static int[] invalidMinutes() {
        return new int[]{1, 15, 29, 31, 45, 59};
    }

    @Test
    void 같은_id를_가진_예약_시간은_동등하다() {
        ReservationTime time = reservationTime().withId(1L);
        ReservationTime differentValueTime = reservationTime().withId(1L);
        assertThat(time).isEqualTo(differentValueTime);
    }

    @Test
    void 다른_id를_가진_예약_시간은_동등하지_않다() {
        ReservationTime time = reservationTime().withId(1L);
        ReservationTime differentValueTime = reservationTime().withId(2L);
        assertThat(time).isNotEqualTo(differentValueTime);
    }

    @Test
    void 아이디가_null이면_다른_객체이다() {
        ReservationTime one = reservationTime();
        ReservationTime other = reservationTime();
        assertThat(one).isNotEqualTo(other);
    }

    @Test
    void withId로_아이디를_부여한_새로운_예약을_생성한다() {
        ReservationTime time = reservationTime();
        ReservationTime saved = time.withId(1L);
        assertThat(saved.getId()).isEqualTo(1L);
    }

    private ReservationTime reservationTime() {
        return new ReservationTime(null, LocalTime.of(12, 0));
    }
}
