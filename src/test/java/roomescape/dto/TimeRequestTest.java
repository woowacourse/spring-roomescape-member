package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TimeRequestTest {

    @Test
    @DisplayName("시간은 빈 값이 들어올 수 없다.")
    void validateNull() {
        assertThatThrownBy(() -> new TimeRequest(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("시간을 선택해야 합니다.");
    }
}
