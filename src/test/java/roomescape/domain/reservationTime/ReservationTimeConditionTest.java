package roomescape.domain.reservationTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.domain.reservation.ReservationCommand;

public class ReservationTimeConditionTest {
    @Test
    @DisplayName("정상 생성 테스트")
    void initTest() {
        String validDate = "2023-08-03";
        ReservationTimeCondition condition = new ReservationTimeCondition(validDate, 1);

        assertThat(condition.date()).isEqualTo(validDate);
    }

    @ParameterizedTest
    @ValueSource(strings = {"2024-02-30", "2024-13-01", "0000-00-00"})
    @DisplayName("잘못된 날짜의 경우 예외 테스트")
    void InvalidDateTest(String invalidDate) {
        assertThatThrownBy(() -> new ReservationTimeCondition(invalidDate, 1))
                .isInstanceOf(ReservationTimeConditionException.class)
                .hasMessage(ErrorMessage.INVALID_DATE_FORMAT.getMessage());
    }

    @ParameterizedTest
    @ValueSource(longs = {0, -1, -100})
    @DisplayName("테마 ID가 0 이하인 경우 예외 테스트")
    void NotPositiveThemeIdTest(long invalidThemeId) {
        assertThatThrownBy(() -> new ReservationCommand("브라운", "2024-05-01", 1L, invalidThemeId))
                .isInstanceOf(ReservationCommandException.class)
                .hasMessage(ErrorMessage.INVALID_TIME_ID_FORMAT.getMessage());
    }
}
