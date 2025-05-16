package roomescape.reservationtime.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.exception.custom.InvalidInputException;

class AvailableReservationTimeTest {

    private static final List<ReservationTime> BOOKED_TIMES = List.of(
            new ReservationTime(1L, LocalTime.of(10, 0)),
            new ReservationTime(2L, LocalTime.of(11, 0)),
            new ReservationTime(3L, LocalTime.of(12, 0))
    );

    private static final ReservationTime RESERVATION_TIME = new ReservationTime(1L, LocalTime.of(10, 0));


    @DisplayName("이용 가능한 예약 시간 생성시, 예약된 시간 목록이 null이면 예외를 던진다")
    @Test
    void createAvailableReservationTimeTest_WhenBookedTimesIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new AvailableReservationTime(null, RESERVATION_TIME))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약된 시간 목록은 null이 될 수 없습니다");
    }

    @DisplayName("이용 가능한 예약 시간 생성시, 예약 시간이 null이면 예외를 던진다")
    @Test
    void createAvailableReservationTimeTest_WhenReservationTimeIsNull() {
        // given // when // then
        assertThatThrownBy(() -> new AvailableReservationTime(BOOKED_TIMES, null))
                .isInstanceOf(InvalidInputException.class)
                .hasMessage("예약 시간은 null이 될 수 없습니다");
    }

    @DisplayName("예약된 시간 목록이 비어 있으면 false를 반환한다")
    @Test
    void isBookedTest_WhenBookedTimesIsEmpty() {
        // given
        final List<ReservationTime> emptyBookedTimes = List.of();

        // when
        final AvailableReservationTime availableReservationTime
                = new AvailableReservationTime(emptyBookedTimes, RESERVATION_TIME);

        // then
        assertThat(availableReservationTime.getBookedStatus()).isFalse();
    }

    @DisplayName("해당 예약 시간이 예약된 시간 목록에 포함되어 있으면 true를 반환한다")
    @Test
    void isBookedTest_WhenBookedTimesContainsReservationTime() {
        // given // when
        final AvailableReservationTime availableReservationTime
                = new AvailableReservationTime(BOOKED_TIMES, RESERVATION_TIME);

        // then
        assertThat(availableReservationTime.getBookedStatus()).isTrue();
    }

    @DisplayName("해당 예약 시간이 예약된 시간 목록에 포함되어 있지 않으면 false를 반환한다")
    @Test
    void isBookedTest_WhenBookedTimesNotContainsReservationTime() {
        // given
        final ReservationTime notBookedTime = new ReservationTime(4L, LocalTime.of(13, 0));

        // when
        final AvailableReservationTime availableReservationTime
                = new AvailableReservationTime(BOOKED_TIMES, notBookedTime);

        // then
        assertThat(availableReservationTime.getBookedStatus()).isFalse();
    }
}
