package roomescape.time.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReservationTimeTest {

    @Test
    void cannotNullTime() {
        // given
        // when
        // then
        assertThatThrownBy(() -> ReservationTime.withoutId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ReservationTime.value 은(는) null일 수 없습니다.");
    }
}
