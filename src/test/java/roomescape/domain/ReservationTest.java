package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
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
        void doesNotThrowWhenPresent() {
            Time time = new Time(1L, NOW.toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);

            assertThatCode(() -> reservation.cancelIfValid(NOW))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("예약 시간이 현재보다 1나노초 전이면 예외를 던진다")
        void throwsWhenJustPast() {
            Time time = new Time(1L, NOW.minusNanos(1).toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);

            assertThatThrownBy(() -> reservation.cancelIfValid(NOW))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    @Nested
    class Update {

        @Test
        @DisplayName("날짜와 시간을 변경한다")
        void updatesDateAndTime() {
            Time time = new Time(1L, NOW.toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);
            Time newTime = new Time(2L, NOW.plusHours(2).toLocalTime());

            reservation.update(NOW.toLocalDate().plusDays(1), newTime);

            assertThat(reservation.getDate()).isEqualTo(NOW.toLocalDate().plusDays(1));
            assertThat(reservation.getTime()).isEqualTo(newTime);
        }
    }

    @Nested
    class IsActive {

        @Test
        @DisplayName("BOOKED 상태이면 true를 반환한다")
        void returnsTrueWhenBooked() {
            Time time = new Time(1L, NOW.toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);

            assertThat(reservation.isActive()).isTrue();
        }

        @Test
        @DisplayName("CANCELED 상태이면 false를 반환한다")
        void returnsFalseWhenCanceled() {
            Time time = new Time(1L, NOW.toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);
            reservation.cancel(NOW);

            assertThat(reservation.isActive()).isFalse();
        }
    }

    @Nested
    class Cancel {

        @Test
        @DisplayName("취소 시 status가 CANCELED로 변경된다")
        void setsStatusToCanceled() {
            Time time = new Time(1L, NOW.toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);

            reservation.cancel(NOW);

            assertThat(reservation.getStatus()).isEqualTo(ReservationStatus.CANCELED);
        }

        @Test
        @DisplayName("취소 시 deletedAt이 now로 설정된다")
        void setsDeletedAt() {
            Time time = new Time(1L, NOW.toLocalTime());
            Reservation reservation = new Reservation("유저", NOW.toLocalDate(), time, THEME);

            reservation.cancel(NOW);

            assertThat(reservation.getDeletedAt()).isEqualTo(NOW);
        }
    }
}
