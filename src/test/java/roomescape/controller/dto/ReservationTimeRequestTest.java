package roomescape.controller.dto;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeRequestTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void null_또는_빈_시간으로_생성시_예외(String startAt) {
        // when & then
        assertThatThrownBy(() -> new ReservationTimeRequest(startAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간은 비어 있을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"12", "25:99"})
    void 올바르지_않은_형식의_시간으로_생성시_예외(String startAt) {
        // when & then
        assertThatThrownBy(() -> new ReservationTimeRequest(startAt))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간 형식이 올바르지 않습니다.");
    }

    @ParameterizedTest
    @ValueSource(strings = {"10:00", "23:59"})
    void 정상_생성_테스트(String startAt) {
        // when
        ReservationTimeRequest result = new ReservationTimeRequest(startAt);

        // then
        assertThat(result.startAt()).isEqualTo(startAt);
    }
}
