package roomescape.time.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReservationTimeTest {

    @Test
    void cannotNullTime() {
        // given
        // when
        // then
        assertAll(() -> {
            assertThatThrownBy(() -> ReservationTime.of(ReservationTimeId.unassigned(), null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("ReservationTime.value 은(는) null일 수 없습니다.");

            assertThatThrownBy(() -> ReservationTime.of(null, LocalTime.now()))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("ReservationTime.id 은(는) null일 수 없습니다.");
        });
    }
}
