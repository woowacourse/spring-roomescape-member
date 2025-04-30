package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.InvalidInputException;

public class TimeRequestTest {

    @Test
    @DisplayName("시간은 빈 값이 들어올 수 없다.")
    void validateNull() {
        assertThatThrownBy(() -> new TimeRequest(null))
            .isInstanceOf(InvalidInputException.class)
            .hasMessage("시간을 선택해라.");
    }
}
