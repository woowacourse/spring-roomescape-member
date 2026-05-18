package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Named.named;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class TimeTest {

    @Nested
    class Constructor {

        static Stream<Named<LocalTime>> validTimes() {
            return Stream.of(
                    named("영업 시작 시간 (10:00)", LocalTime.of(10, 0)),
                    named("영업 종료 시간 (22:00)", LocalTime.of(22, 0))
            );
        }

        static Stream<Named<LocalTime>> invalidTimes() {
            return Stream.of(
                    named("영업 시작 전 (09:59)", LocalTime.of(9, 59)),
                    named("영업 종료 후 (22:01)", LocalTime.of(22, 1))
            );
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("validTimes")
        @DisplayName("영업 시간 경계값으로 생성에 성공한다")
        void createsTime(LocalTime startAt) {
            assertThat(new Time(startAt).getStartAt()).isEqualTo(startAt);
        }

        @ParameterizedTest(name = "{0}")
        @MethodSource("invalidTimes")
        @DisplayName("영업 시간 외이면 예외를 던진다")
        void throwsWhenOutOfBusinessHours(LocalTime startAt) {
            assertThatThrownBy(() -> new Time(startAt))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("id 없이 생성하면 id가 null이다")
        void createsTimeWithNullId() {
            assertThat(new Time(LocalTime.of(13, 0)).getId()).isNull();
        }
    }

    @Nested
    class IsReservationBefore {

        private static final LocalDate DATE = LocalDate.of(2026, 6, 1);
        private static final Time TIME = new Time(1L, LocalTime.of(13, 0));

        @Test
        @DisplayName("예약 시간이 기준 시각보다 과거이면 true를 반환한다")
        void returnsTrueWhenReservationIsPast() {
            LocalDateTime now = LocalDateTime.of(DATE, LocalTime.of(14, 0));

            assertThat(TIME.isReservationBefore(now, DATE)).isTrue();
        }

        @Test
        @DisplayName("예약 시간이 기준 시각과 같으면 false를 반환한다")
        void returnsFalseWhenReservationIsPresent() {
            LocalDateTime now = LocalDateTime.of(DATE, LocalTime.of(13, 0));

            assertThat(TIME.isReservationBefore(now, DATE)).isFalse();
        }

        @Test
        @DisplayName("예약 시간이 기준 시각보다 미래이면 false를 반환한다")
        void returnsFalseWhenReservationIsFuture() {
            LocalDateTime now = LocalDateTime.of(DATE, LocalTime.of(12, 0));

            assertThat(TIME.isReservationBefore(now, DATE)).isFalse();
        }
    }
}
