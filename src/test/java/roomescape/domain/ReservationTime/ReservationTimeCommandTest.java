package roomescape.domain.ReservationTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationCommandException;

class ReservationTimeCommandTest {
    @Test
    @DisplayName("정상 생성 테스트")
    void initTest() {
        String validTime = "14:00";
        ReservationTimeCommand command = new ReservationTimeCommand(validTime);

        assertThat(command.startAt()).isEqualTo(validTime);
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    @DisplayName("시작 시간이 비어있거나 공백인 경우 예외 테스트")
    void EmptyStartTimeTest(String invalidTime) {
        assertThatThrownBy(() -> new ReservationTimeCommand(invalidTime))
                .isInstanceOf(ReservationCommandException.class)
                .hasMessage(ErrorMessage.INVALID_START_TIME_NULL.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"24:00", "12:60", "25:01", "9:00", "오후 2시", "14-00"})
    @DisplayName("잘못된 형식의 시간인 경우 예외 테스트")
    void InvalidTimeFormatTest(String invalidTime) {
        assertThatThrownBy(() -> new ReservationTimeCommand(invalidTime))
                .isInstanceOf(ReservationCommandException.class)
                .hasMessage(ErrorMessage.INVALID_START_TIME_FORMAT.getMessage());
    }
}
