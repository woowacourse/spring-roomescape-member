package roomescape.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " "})
    void 이름이_null_또는_blank이면_예외(String name) {
        // given
        ReservationTime time = new ReservationTime(1L, "10:00");

        // when & then
        assertThatThrownBy(() -> new Reservation(null, name, "2026-05-02", time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 비어 있을 수 없습니다.");
    }

    @Test
    void 이름이_255자를_초과하면_예외() {
        // given
        String name = "a".repeat(256);
        ReservationTime time = new ReservationTime(1L, "10:00");

        // when & then
        assertThatThrownBy(() -> new Reservation(null, name, "2026-05-02", time))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 이름은 255자를 넘을 수 없습니다.");
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 255})
    void 예약_생성_성공_테스트(int count) {
        // given
        String name = "a".repeat(count);
        ReservationTime time = new ReservationTime(1L, "10:00");

        // when
        Reservation result = new Reservation(null, name, "2026-05-02", time);

        // then
        assertThat(result.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(strings = {"", " ", "2026-99-99"})
    void 유효하지_않은_날짜로_예약_생성시_예외(String date) {
        // given
        ReservationTime time = new ReservationTime(1L, "10:00");

        // when & then
        assertThatThrownBy(() -> new Reservation(null, "구구", date, time))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 예약_시간이_null이면_예약_생성시_예외() {
        // when & then
        assertThatThrownBy(() -> new Reservation(null, "홍길동", "2026-05-02", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 예약 시간은 비어있을 수 없습니다.");
    }
}
