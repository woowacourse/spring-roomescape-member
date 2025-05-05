package roomescape.time.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeTest {

    @Test
    @DisplayName("Time 객체를 정상적으로 생성할 수 있다.")
    void test5() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);

        // when
        Time time = new Time(1L, startAt);

        // then
        assertThat(1L).isEqualTo(time.id());
        assertThat(startAt).isEqualTo(time.startAt());
    }

    @Test
    @DisplayName("DB에 저장하기 이전 기본 id로 Time 객체를 생성할 수 있다.")
    void test6() {
        // given
        Long notSavedDefaultId = 0L;
        LocalTime startAt = LocalTime.of(9, 30);

        // when
        Time time = Time.createBeforeSaved(startAt);

        // then
        assertThat(time.id()).isEqualTo(notSavedDefaultId);
        assertThat(time.startAt()).isEqualTo(startAt);
    }

    @Test
    @DisplayName("id가 null인 경우 예외가 발생한다.")
    void test7() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);

        // when & then
        assertThatThrownBy(() -> new Time(null, startAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] id가 null이어서는 안 됩니다.");
    }

    @Test
    @DisplayName("시작 시간이 null인 경우 예외가 발생한다.")
    void test8() {
        // given
        Long id = 1L;

        // when & then
        assertThatThrownBy(() -> new Time(id, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시작 시간이 null이어서는 안 됩니다.");
    }

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
