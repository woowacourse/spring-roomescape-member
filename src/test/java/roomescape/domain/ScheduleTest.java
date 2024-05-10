package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidReservationException;

class ScheduleTest {
    @DisplayName("일정은 현재 이전으로 설정하면 예외를 던진다.")
    @Test
    void invalidTermSchedule() {
        assertThatThrownBy(() -> new Schedule(new ReservationDate("2024-03-01"), new ReservationTime("14:00")))
                .isInstanceOf(InvalidReservationException.class)
                .hasMessage("현재보다 이전으로 일정을 설정할 수 없습니다.");
    }
}
