package roomescape.time.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationTimeTest {

    private ReservationTime reservationTime;

    @BeforeEach
    void setUp() {
        reservationTime = ReservationTime.load(1L, LocalTime.of(10, 0), false);
    }

    @Test
    @DisplayName("예약 시간 id를 가져온다.")
    void getId() {
        //given
        Long expected = 1L;

        //when
        Long actual = reservationTime.getId();

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("예약 시작 시간을 가져온다.")
    void getStartAt() {
        //given
        LocalTime expected = LocalTime.of(10, 0);

        //when
        LocalTime actual = reservationTime.getStartAt();

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("예약 시작 시간이 유효하지 않은 경우 예외가 발생한다.")
    void validate_startAt() {
        assertThatThrownBy(() -> ReservationTime.load(1L, null, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시작 시간은 필수입니다.");
    }

}
