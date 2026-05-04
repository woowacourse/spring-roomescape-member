package roomescape.domain;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "12", "99:99"})
    void 유효하지_않은_시간으로_예약시간_생성시_예외(String startAt) {
        // when & then
        assertThatThrownBy(() -> new ReservationTime(1L, startAt))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"10:00", "23:59"})
    void 예약시간_생성_성공_테스트(String startAt) {
        // when
        ReservationTime result = new ReservationTime(null, startAt);

        // then
        assertThat(result.getStartAt()).isEqualTo(startAt);
    }
}
