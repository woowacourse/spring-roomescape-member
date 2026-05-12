package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import roomescape.domain.vo.Name;

class ReservationTest {

    private static final Theme THEME = new Theme(1L, new Name("방탈출"), "http://example.com", "설명");
    private static final LocalDateTime NOW = LocalDateTime.of(2026, 5, 12, 12, 0);

    @Nested
    class ValidateCreate {

        @Test
        @DisplayName("예약 시간이 현재이면 예외를 던지지 않는다")
        void doesNotThrowWhenJustFuture() {
            Time time = new Time(1L, NOW.toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);

            assertThatCode(() -> reservation.validateCreate(NOW))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("예약 시간이 현재보다 1나노초 전이면 예외를 던진다")
        void throwsWhenJustPast() {
            Time time = new Time(1L, NOW.minusNanos(1).toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);

            assertThatThrownBy(() -> reservation.validateCreate(NOW))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class ValidateCancel {

        @Test
        @DisplayName("예약 시간이 현재이면 예외를 던지지 않는다")
        void doesNotThrowWhenJustFuture() {
            Time time = new Time(1L, NOW.toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);

            assertThatCode(() -> reservation.validateCancel(NOW))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("예약 시간이 현재보다 1나노초 전이면 예외를 던진다")
        void throwsWhenJustPast() {
            Time time = new Time(1L, NOW.minusNanos(1).toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);

            assertThatThrownBy(() -> reservation.validateCancel(NOW))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
