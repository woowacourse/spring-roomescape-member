package roomescape.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class ReservationTimeTest {
    @Test
    void 정상형식_시간_생성_테스트() {
        // when, then
        assertDoesNotThrow(() -> new ReservationTime(null, "13:24"));
    }

    @ParameterizedTest
    @ValueSource(strings = { "12-30", "12.30", "9:30", "12:5", "12:30:15",
            "25:00", "12:60", "13:99", "-01:30",
            "오전 10:30", "10:30 AM", "ten:30", "12 : 30", "!!:!!",
            "", " ", "2023-10-27"})
    void 형식외_시간_생성시_예외가_발생한다(String time) {
        // when, then
        assertThatThrownBy(() -> new ReservationTime(null, time))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void 동일_id_동일_객체_테스트() {
        // given
        ReservationTime time1 = new ReservationTime(1L, "12:30");
        ReservationTime time2 = new ReservationTime(1L, "12:30");

        // when, then
        assertThat(time1).isEqualTo(time2);
    }

    @Test
    void 다른_id_다른_객체_테스트() {
        // given
        ReservationTime time1 = new ReservationTime(1L, "12:30");
        ReservationTime time2 = new ReservationTime(2L, "13:30");

        // when, then
        assertThat(time1).isNotEqualTo(time2);
    }
}