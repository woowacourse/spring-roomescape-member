package roomescape.time.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
        Long actual = reservationTime.id();

        //then
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("예약 시작 시간을 가져온다.")
    void getStartAt() {
        //given
        LocalTime expected = LocalTime.of(10, 0);

        //when
        LocalTime actual = reservationTime.startAt();

        //then
        assertEquals(expected, actual);
    }


    @Test
    @DisplayName("두 예약 객체의 동등성을 비교한다.")
    void equals() {
        //given & when
        ReservationTime otherReservationTime = ReservationTime.load(1L, LocalTime.of(20, 0), false);

        //then
        assertEquals(reservationTime, otherReservationTime);
    }

    @Test
    @DisplayName("아직 DB에 추가되지 않은 예약끼리와는 동등하지 않다.")
    void equals_null_id() {
        //given & when
        ReservationTime reservationTime1 = ReservationTime.create(LocalTime.of(10, 0));
        ReservationTime reservationTime2 = ReservationTime.create(LocalTime.of(10, 0));

        //then
        assertNotEquals(reservationTime1, reservationTime2);
    }

    @Test
    @DisplayName("예약 시작 시간이 유효하지 않은 경우 예외가 발생한다.")
    void validate_startAt() {
        assertThatThrownBy(() -> ReservationTime.load(1L, null, false))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("예약 시작 시간은 필수입니다.");
    }

}
