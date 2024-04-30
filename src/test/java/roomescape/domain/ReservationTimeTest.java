package roomescape.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class ReservationTimeTest {

    @DisplayName("시간이 형식에 맞지 않을 때 예외를 던진다.")
    @Test
    void validateTimeTest_whenTimeFormatIsNotMatch() {
        String time = "9-00";

        assertThatThrownBy(() -> new ReservationTime(null, time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("시간(%s)이 HH:mm 형식에 맞지 않습니다.".formatted(time));
    }

    @DisplayName("시간이 형식에 맞으면 정상적으로 객체가 생성된다.")
    @Test
    void timeTest_whenTimeFormatIsMatch() {
        String time = "09:00";

        assertThatCode(() -> new ReservationTime(null, time))
                .doesNotThrowAnyException();
    }

    @DisplayName("시간이 비어있을 때 예외를 던진다.")
    @Test
    void validateTimeTest_whenTimeIsNull() {
        assertThatThrownBy(() -> new ReservationTime(1L, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("인자 중 null 값이 존재합니다.");
    }

    @DisplayName("시간을 통해 특정 시간대 이전임을 알 수 있다.")
    @Test
    void isAfterTest_whenTimeIsBefore() {
        ReservationTime time = new ReservationTime(1L, "09:00");
        LocalTime localTime = LocalTime.of(9, 1);
        assertThat(time.isAfter(localTime)).isFalse();
    }

    @DisplayName("시간을 통해 특정 시간대 이후임을 알 수 있다.")
    @Test
    void isAfterTest_whenTimeIsAfter() {
        ReservationTime time = new ReservationTime(1L, "09:00");
        LocalTime localTime = LocalTime.of(8, 59);
        assertThat(time.isAfter(localTime)).isTrue();
    }
}
