package roomescape.time.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.*;

class TimeRequestTest {

    @Test
    @DisplayName("시작 시간이 null이면 예외가 발생한다")
    void test1() {
        assertThatThrownBy(() -> new TimeRequest(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간을 입력해주세요.");
    }

    @Test
    @DisplayName("시작 시간이 유효하면 정상적으로 생성된다")
    void test2() {
        // given
        LocalTime validTime = LocalTime.of(10, 30);

        // when
        TimeRequest timeRequest = new TimeRequest(validTime);

        // then
        assertThat(timeRequest.startAt()).isEqualTo(validTime);
    }
}
