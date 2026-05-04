package roomescape.domain.Reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.ErrorMessage;
import roomescape.exception.ReservationCommandException;

class ReservationCommandTest {

    @Test
    @DisplayName("정상 생성 테스트")
    void initTest() {
        String name = "브라운";
        String date = "2024-05-18";
        long timeId = 1L;

        ReservationCommand command = new ReservationCommand(name, date, timeId);

        assertAll(
                () -> assertThat(command.name()).isEqualTo(name),
                () -> assertThat(command.date()).isEqualTo(date),
                () -> assertThat(command.timeId()).isEqualTo(timeId)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"", " ", "  "})
    @DisplayName("이름이 비어있거나 공백인 경우 예외 테스트")
    void fail_name_blank(String invalidName) {
        assertThatThrownBy(() -> new ReservationCommand(invalidName, "2023-05-18", 1L))
                .isInstanceOf(ReservationCommandException.class)
                .hasMessage(ErrorMessage.INVALID_NAME_BLANK.getMessage());
    }

    @Test
    @DisplayName("이름이 20자를 초과한 경우 예외 테스트")
    void fail_name_length() {
        String longName = "a".repeat(21);

        assertThatThrownBy(() -> new ReservationCommand(longName, "2023-05-18", 1L))
                .isInstanceOf(ReservationCommandException.class)
                .hasMessage(ErrorMessage.INVALID_NAME_LENGTH.getMessage());
    }

    @Test
    @DisplayName("날짜가 null인 경우 예외 테스트")
    void fail_date_null() {
        assertThatThrownBy(() -> new ReservationCommand("브라운", null, 1L))
                .isInstanceOf(ReservationCommandException.class)
                .hasMessage(ErrorMessage.INVALID_DATE_NULL.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-02-30", "2024-13-01", "not-a-date", "24-05-01"})
    @DisplayName("잘못된 형식의 날짜인 경우 예외 테스트")
    void fail_date_format(String invalidDate) {
        assertThatThrownBy(() -> new ReservationCommand("브라운", invalidDate, 1L))
                .isInstanceOf(ReservationCommandException.class)
                .hasMessage(ErrorMessage.INVALID_DATE_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -100})
    @DisplayName("시간 ID가 0 이하인 경우 예외 테스트")
    void fail_timeId_format(long invalidTimeId) {
        assertThatThrownBy(() -> new ReservationCommand("브라운", "2024-05-01", invalidTimeId))
                .isInstanceOf(ReservationCommandException.class)
                .hasMessage(ErrorMessage.INVALID_TIME_ID_FORMAT.getMessage());
    }
}
