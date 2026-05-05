package roomescape.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    private static final long DEFAULT_ID = 1;

    @Nested
    class 시간_정보를_검증한다 {
        @Test
        void 생성_시_시간_정보가_없다면_예외를_던진다() {
            assertThatThrownBy(() -> ReservationTime.create(
                    null
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약 시간엔 시간 정보가 존재해야 합니다.");
        }

        @Test
        void 불러올_때_시간_정보가_없다면_예외를_던진다() {
            assertThatThrownBy(() -> ReservationTime.retrieve(
                    DEFAULT_ID,
                    null
            )).isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("예약 시간엔 시간 정보가 존재해야 합니다.");
        }
    }
}
