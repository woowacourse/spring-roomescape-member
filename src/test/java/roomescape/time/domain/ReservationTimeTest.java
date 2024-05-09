package roomescape.time.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import roomescape.exception.RoomEscapeException;

class ReservationTimeTest {

    @DisplayName("존재하지 않는 시간을 선택했을 경우 예외가 발생한다")
    @Test
    void validateTimeExist() {
        assertAll(
                () -> assertThatThrownBy(() -> new ReservationTime(1L, (String) null))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage("형식에 맞지 않은 시간입니다."),
                () -> assertThatThrownBy(() -> new ReservationTime(1L, "25:00"))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage("형식에 맞지 않은 시간입니다."),
                () -> assertThatThrownBy(() -> new ReservationTime(1L, "15:68"))
                        .isInstanceOf(RoomEscapeException.class)
                        .hasMessage("형식에 맞지 않은 시간입니다.")
        );
    }
}
