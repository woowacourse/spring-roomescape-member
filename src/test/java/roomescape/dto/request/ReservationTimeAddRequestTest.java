package roomescape.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.ReservationTime;

class ReservationTimeAddRequestTest {

    @Test
    @DisplayName("예약 시간 추가 요청시 전달받은 데이터를 바탕으로 ReservationTime 객체를 생성한다.")
    void toReservationGetTime() {
        LocalTime time = LocalTime.parse("10:00");
        ReservationTimeAddRequest reservationTimeAddRequest = new ReservationTimeAddRequest(time);
        ReservationTime expected = new ReservationTime(time);

        ReservationTime reservationTime = reservationTimeAddRequest.toReservationTime();

        assertThat(reservationTime).isEqualTo(expected);
    }
}
