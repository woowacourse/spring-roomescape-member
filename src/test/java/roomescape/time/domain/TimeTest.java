package roomescape.time.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.model.RoomEscapeException;

public class TimeTest {

    @Test
    @DisplayName("시간이 null일 경우 예외가 발생한다.")
    void validation_ShouldThrowException_WhenStartAtIsNull() {
        Throwable nullStartAt = assertThrows(
                RoomEscapeException.class, () -> new Time(null));
        assertEquals("존재하는 시간이 없습니다.", nullStartAt.getMessage());
    }

    @Test
    @DisplayName("추가하는 시간이 운영 시간보다 빠를 경우 예외가 발생한다.")
    void validation_ShouldThrowException_WhenStartAtIsBeforeOpeningHour() {
        Throwable beforeOpenTime = assertThrows(
                RoomEscapeException.class, () -> new Time(LocalTime.of(7, 59)));
        assertEquals("운영 시간 외의 예약 시간 요청입니다.", beforeOpenTime.getMessage());
    }

    @Test
    @DisplayName("추가하려는 시간이 운영 시간보다 느릴 경우 예외가 발생한다.")
    void validation_ShouldThrowException_WhenStartAtIsAfterEndHour() {
        Throwable afterCloseTime = assertThrows(
                RoomEscapeException.class, () -> new Time(LocalTime.of(23, 1)));
        assertEquals("운영 시간 외의 예약 시간 요청입니다.", afterCloseTime.getMessage());
    }
}
