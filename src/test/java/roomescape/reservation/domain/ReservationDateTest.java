package roomescape.reservation.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ReservationDateTest {

    @Test
    void cannotNullDate() {
        // given
        // when
        // then
        assertThatThrownBy(() -> ReservationDate.from(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("ReservationDate.value 은(는) null일 수 없습니다.");
    }
}
