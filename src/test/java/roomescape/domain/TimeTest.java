package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Named.named;

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
}
