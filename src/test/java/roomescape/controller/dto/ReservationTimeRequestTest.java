package roomescape.controller.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTimeRequestTest {

    @Test
    void null로_생성시_예외() {
        // when & then
        assertThatThrownBy(() -> new ReservationTimeRequest(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 시간은 비어 있을 수 없습니다.");
    }

    @Test
    void 정상_생성_테스트() {
        // given
        LocalTime startAt = LocalTime.of(10, 0);

        // when
        ReservationTimeRequest result = new ReservationTimeRequest(startAt);

        // then
        assertThat(result.startAt()).isEqualTo(startAt);
    }
}
