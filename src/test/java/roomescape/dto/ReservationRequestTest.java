package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ReservationRequestTest {

    @Test
    @DisplayName("날짜는 빈 값이 들어올 수 없다.")
    void validateNullOfDate() {
        assertThatThrownBy(() -> new ReservationRequest(null, "프리", 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("값을 모두 선택해야 합니다.");
    }

    @Test
    @DisplayName("이름은 빈 값이 들어올 수 없다.")
    void validateNullOfName() {
        assertThatThrownBy(() -> new ReservationRequest(LocalDate.of(2025, 4, 29), null, 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("값을 모두 선택해야 합니다.");
    }

    @Test
    @DisplayName("시간은 빈 값이 들어올 수 없다.")
    void validateNullOfTime() {
        assertThatThrownBy(() -> new ReservationRequest(LocalDate.of(2025, 4, 29), "프리", null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("값을 모두 선택해야 합니다.");
    }

    @Test
    @DisplayName("이름은 한 글자 이상이어야 한다")
    void validateNameLength() {
        assertThatThrownBy(() -> new ReservationRequest(LocalDate.of(2025, 4, 29), "", 1L))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("이름은 한 글자 이상이어야 합니다.");
    }
}
