package roomescape.domain.reservationTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.exception.InvalidRequestValueException;

class ReservationTimeWithAvailableTest {

    @Test
    @DisplayName("정상 생성 테스트")
    void initTest() {
        long id = 1L;
        LocalTime startAt = LocalTime.of(10, 0);

        ReservationTimeWithAvailable timeWithAvailable = new ReservationTimeWithAvailable(id, startAt, true);

        assertAll(
                () -> assertThat(timeWithAvailable.id()).isEqualTo(id),
                () -> assertThat(timeWithAvailable.startAt()).isEqualTo(startAt),
                () -> assertThat(timeWithAvailable.isAvailable()).isTrue()
        );
    }

    @Test
    @DisplayName("String 형식의 시간 정상 생성 테스트")
    void initByStringTest() {
        String startAtStr = "22:00";

        ReservationTimeWithAvailable timeWithAvailable = new ReservationTimeWithAvailable(1L, startAtStr, false);

        assertThat(timeWithAvailable.startAt()).isEqualTo(LocalTime.of(22, 0));
        assertThat(timeWithAvailable.isAvailable()).isFalse();
    }

    @Test
    @DisplayName("LocalTime을 null로 전달하면 예외 발생")
    void nullLocalTimeTest() {
        assertThatThrownBy(() -> new ReservationTimeWithAvailable(1L, (LocalTime) null, true))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("시작 시간은 필수입니다.");
    }

    @Test
    @DisplayName("시간 String을 null로 전달하면 예외가 발생")
    void nullStringTimeTest() {
        assertThatThrownBy(() -> new ReservationTimeWithAvailable(1L, (String) null, true))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("시작 시간은 필수입니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"9:30", "25:00", "invalid-time"})
    @DisplayName("유효하지 않은 시간 형식 문자열 예외 발생")
    void invalidStringFormatTest(String invalidTime) {
        assertThatThrownBy(() -> new ReservationTimeWithAvailable(1L, invalidTime, true))
                .isInstanceOf(InvalidRequestValueException.class)
                .hasMessage("유효하지 않은 시간입니다.");
    }
}