package roomescape.dto.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

class ReservationAddRequestTest {

    @Test
    @DisplayName("예약 요청시 전달받은 데이터를 바탕으로 Reservation 객체를 생성한다.")
    void createReservationFromReservationAddRequest() {
        String name = "브라운";
        LocalDate date = LocalDate.parse("2024-04-01");
        Long timeId = 1L;
        ReservationAddRequest reservationAddRequest = new ReservationAddRequest(name, date, timeId);
        Reservation expected = new Reservation(name, date, new ReservationTime(timeId));

        Reservation reservation = reservationAddRequest.toReservation();

        assertThat(reservation).isEqualTo(expected);
    }
}
