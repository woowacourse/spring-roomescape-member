package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import roomescape.dto.request.ReservationPostRequestByUser;

class ReservationPostRequestByUserTest {

    @Test
    @DisplayName("date 필드가 null일 경우 예외가 발생한다.")
    void failIfDateFieldIsNull() {
        assertThatThrownBy(() -> {
            new ReservationPostRequestByUser(null, 1L, 1L);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 date 입력입니다.");
    }

    @ParameterizedTest
    @DisplayName("timeId 필드가 1 미만일 경우 예외가 발생한다.")
    @ValueSource(longs = {0, -1})
    void failIfTimeIdFieldIsNull(long timeId) {
        assertThatThrownBy(() -> {
            new ReservationPostRequestByUser(LocalDate.of(2025, 4, 30), timeId, 1L);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 timeId 입력입니다.");
    }

    @ParameterizedTest
    @DisplayName("themeId 필드가 1 미만일 경우 예외가 발생한다.")
    @ValueSource(longs = {0, -1})
    void failIfThemeIdFieldIsInvalid(Long themeId) {
        assertThatThrownBy(() -> {
            new ReservationPostRequestByUser(LocalDate.of(2025, 4, 30), 1L, themeId);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("잘못된 themeId 입력입니다.");
    }
}
