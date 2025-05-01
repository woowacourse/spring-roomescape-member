package roomescape.time.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeTest {

    @Test
    @DisplayName("시작 시간이 비교 시간보다 이전이면 true를 반환한다")
    void test1() {
        // given
        LocalTime startTime = LocalTime.of(10, 0);
        Time time = new Time(1L, startTime);
        LocalTime targetTime = LocalTime.of(12, 0);

        // when & then
        assertThat(time.isBefore(targetTime)).isTrue();
    }

    @Test
    @DisplayName("시작 시간이 비교 시간 이후이면 false를 반환한다")
    void test2() {
        // given
        LocalTime startTime = LocalTime.of(14, 0);
        Time time = new Time(1L, startTime);
        LocalTime targetTime = LocalTime.of(12, 0);

        // when & then
        assertThat(time.isBefore(targetTime)).isFalse();
    }

    @Test
    @DisplayName("시작 시간이 비교 시간과 같으면 false를 반환한다")
    void test3() {
        // given
        LocalTime startTime = LocalTime.of(12, 0);
        Time time = new Time(1L, startTime);
        LocalTime targetTime = LocalTime.of(12, 0);

        // when & then
        assertThat(time.isBefore(targetTime)).isFalse();
    }
}
