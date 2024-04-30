package roomescape.dto;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeRequestTest {

    @DisplayName("빈 값 입력 검증")
    @Test
    void validateNotNullFail() {
                assertThatCode(() -> new ReservationTimeRequest(null))
                        .isInstanceOf(IllegalArgumentException.class)
                        .hasMessage("빈 값은 입력할 수 없다.");
    }
}
