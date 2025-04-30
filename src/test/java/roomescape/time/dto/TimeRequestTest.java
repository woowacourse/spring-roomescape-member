package roomescape.time.dto;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class TimeRequestTest {

    @DisplayName("시간이 null일 경우 예외가 발생한다.")
    @Test
    void test1() {
        assertThatThrownBy(() -> new TimeRequest(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
