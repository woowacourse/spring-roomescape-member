package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class ReservationTimeTest {

    @Test
    void 올바른_형식의_시간으로_생성한다() {
        // given
        ReservationTime time = new ReservationTime("13:24");

        // when & then
        assertThat(time.getStartAt()).isEqualTo("13:24");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12-30", "12.30", "9:30", "12:5", "12:30:15",
        "25:00", "12:60", "13:99", "-01:30",
        "오전 10:30", "10:30 AM", "ten:30", "12 : 30", "!!:!!",
        "", " ", "2023-10-27"})
    void 형식외_시간_생성시_예외가_발생한다(String time) {
        // when & then
        assertThatThrownBy(() -> new ReservationTime(time))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 같은_id를_가진_예약_시간은_동등하다() {
        // given
        ReservationTime time = reservationTime().withId(1L);
        ReservationTime differentValueTime = reservationTime().withId(1L);

        // when & then
        assertThat(time).isEqualTo(differentValueTime);
    }

    @Test
    void 다른_id를_가진_예약_시간은_동등하지_않다() {
        // given
        ReservationTime time = reservationTime().withId(1L);
        ReservationTime differentValueTime = reservationTime().withId(2L);

        // when & then
        assertThat(time).isNotEqualTo(differentValueTime);
    }

    @Test
    void 아이디가_null이면_다른_객체이다() {
        // given
        ReservationTime one = reservationTime();
        ReservationTime other = reservationTime();

        // when & then
        assertThat(one).isNotEqualTo(other);
    }

    @Test
    void withId로_아이디를_부여한_새로운_예약을_생성한다() {
        // given
        ReservationTime time = reservationTime();

        // when
        ReservationTime saved = time.withId(1L);

        // then
        assertThat(saved.getId()).isEqualTo(1L);
    }

    private ReservationTime reservationTime() {
        return new ReservationTime(
            null,
            LocalTime.of(12, 30)
        );
    }
}