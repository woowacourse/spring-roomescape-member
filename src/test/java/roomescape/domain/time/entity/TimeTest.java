package roomescape.domain.time.entity;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class TimeTest {

    @Nested
    @DisplayName("과거 여부 테스트")
    class IsPast {

        Clock fixedClock;

        @BeforeEach
        void setUp() {
            fixedClock = Clock.fixed(
                Instant.parse("2026-01-01T10:00:00Z"),
                ZoneId.of("UTC")
            );
        }

        @Test
        @DisplayName("과거 시간인 경우 true를 반환한다.")
        void 성공1() {
            LocalTime startAt = LocalTime.of(9, 59);
            Time time = Time.create(startAt);
            boolean expected = true;

            boolean actual = time.isPast(fixedClock);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("현재 시간인 경우 false를 반환한다.")
        void 성공2() {
            LocalTime startAt = LocalTime.of(10, 0);
            Time time = Time.create(startAt);
            boolean expected = false;

            boolean actual = time.isPast(fixedClock);

            assertThat(actual).isEqualTo(expected);
        }

        @Test
        @DisplayName("미래 시간인 경우 false를 반환한다.")
        void 성공3() {
            LocalTime startAt = LocalTime.of(10, 1);
            Time time = Time.create(startAt);
            boolean expected = false;

            boolean actual = time.isPast(fixedClock);

            assertThat(actual).isEqualTo(expected);
        }
    }
}