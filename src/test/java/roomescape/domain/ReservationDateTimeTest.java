package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReservationDateTimeTest {

    @Nested
    class 예약_가능한_일정인지_여부를_반환한다 {

        @Test
        void 일정이_현재보다_미래라면_true를_반환한다() {
            // given
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime futureDateTime = current.plusDays(1);

            ReservationDateTime futureSchedule = new ReservationDateTime(futureDateTime);

            // when
            boolean reservationAvailable = futureSchedule.isAvailable(current);

            // then
            assertThat(reservationAvailable).isTrue();
        }

        @Test
        void 일정이_현재와_동일하다면_false를_반환한다() {
            // given
            LocalDateTime current = LocalDateTime.now();
            ReservationDateTime currentSchedule = new ReservationDateTime(current);

            // when
            boolean reservationAvailable = currentSchedule.isAvailable(current);

            // then
            assertThat(reservationAvailable).isFalse();
        }

        @Test
        void 일정이_현재보다_과거라면_false를_반환한다() {
            // given
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime pastDateTime = current.minusDays(1);

            ReservationDateTime pastSchedule = new ReservationDateTime(pastDateTime);

            // when
            boolean reservationAvailable = pastSchedule.isAvailable(current);

            // then
            assertThat(reservationAvailable).isFalse();
        }
    }

    @Nested
    class 예약_가능한_일정인지_검증한다 {

        @Test
        void 일정이_현재보다_미래라면_예외를_던지지_않는다() {
            // given
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime futureDateTime = current.plusDays(1);

            ReservationDateTime futureSchedule = new ReservationDateTime(futureDateTime);

            // when and then
            assertThatNoException().isThrownBy(() -> futureSchedule.validateAvailable(current));
        }

        @Test
        void 일정이_현재와_동일하다면_예외를_던진다() {
            // given
            LocalDateTime current = LocalDateTime.now();
            ReservationDateTime currentSchedule = new ReservationDateTime(current);

            // when and then
            assertThatThrownBy(() -> currentSchedule.validateAvailable(current))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("미래 시간만 예약할 수 있습니다.");
        }

        @Test
        void 일정이_현재보다_과거라면_예외를_던진다() {
            // given
            LocalDateTime current = LocalDateTime.now();
            LocalDateTime pastDateTime = current.minusDays(1);

            ReservationDateTime pastSchedule = new ReservationDateTime(pastDateTime);

            // when
            assertThatThrownBy(() -> pastSchedule.validateAvailable(current))
                    .isInstanceOf(IllegalStateException.class)
                    .hasMessageContaining("미래 시간만 예약할 수 있습니다.");
        }
    }
}
