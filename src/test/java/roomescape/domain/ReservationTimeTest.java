package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    private static final UUID DEFAULT_ID = UUID.fromString("00000000-0000-0000-0000-000000000001");

    @Test
    void 식별자가_없다면_예외를_던진다() {
        assertThatThrownBy(() -> new ReservationTime(
                null,
                LocalTime.NOON
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간엔 식별자가 존재해야 합니다.");
    }

    @Test
    void 시작_시간이_없다면_예외를_던진다() {
        assertThatThrownBy(() -> new ReservationTime(
                DEFAULT_ID,
                null
        )).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시간엔 시간 정보가 존재해야 합니다.");
    }
}
