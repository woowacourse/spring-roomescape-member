package roomescape.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TimeSlotTest {

    @Test
    @DisplayName("예약 생성 시 id가 아닌 모든 값들이 존재하지 않으면 예외가 발생한다")
    void anyValueNullException() {
        // given & when & then
        assertThatThrownBy(() -> new TimeSlot(1L, null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("타임 슬롯이 주어진 시간보다 이전인지 확인한다")
    void isBefore() {
        // given
        TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(10, 0));

        // when
        boolean isBefore = timeSlot.isBefore(LocalTime.of(11, 0));

        // then
        assertThat(isBefore).isTrue();
    }

    @Test
    @DisplayName("타임 슬롯이 주어진 타임 슬롯과 같은 시간인지 확인한다")
    void isSameDateTime() {
        // given
        TimeSlot timeSlot = new TimeSlot(1L, LocalTime.of(10, 0));
        TimeSlot otherTimeSlot = new TimeSlot(1L, LocalTime.of(10, 0));

        // when
        boolean isSameDateTime = timeSlot.isSameTimeSlot(otherTimeSlot);

        // then
        assertThat(isSameDateTime).isTrue();
    }
}
