package roomescape.reservationTime.domain;

import java.time.LocalTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.reservationTime.fixture.ReservationTimeFixture;

class ReservationTimeTest {

    @Test
    @DisplayName("예약 시간이 같으면 true를 반환한다")
    void isSameTime_true() {
        // given
        int dummyHour = 11;
        int dummyMinute = 13;

        LocalTime inputStartAt = LocalTime.of(dummyHour, dummyMinute);
        LocalTime reservationStartAt = LocalTime.of(dummyHour, dummyMinute);

        ReservationTime inputReservationTime = ReservationTimeFixture.create(inputStartAt);
        ReservationTime reservationTime = ReservationTimeFixture.create(reservationStartAt);

        // when
        boolean actual = reservationTime.isSameTime(inputReservationTime);

        // then
        Assertions.assertThat(actual).isTrue();
    }
}
