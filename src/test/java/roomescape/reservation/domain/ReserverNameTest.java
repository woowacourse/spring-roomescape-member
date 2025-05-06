package roomescape.reservation.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

class ReserverNameTest {

    @Test
    @DisplayName("예약자 이름은 비어있을 수 없다")
    void cannotEmptyReserverName() {
        // given
        // when
        // then
        assertAll(() -> {
            assertThatThrownBy(() -> ReserverName.from(null))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("ReserverName.value 은(는) null일 수 없습니다.");
            assertThatThrownBy(() -> ReserverName.from(""))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("ReserverName.value 은(는) 비어있을 수 없습니다.");
        });
    }
}
