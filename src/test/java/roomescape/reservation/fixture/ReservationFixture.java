package roomescape.reservation.fixture;

import java.time.LocalDate;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.dto.ReservationReqDto;
import roomescape.reservationTime.domain.ReservationTime;

public class ReservationFixture {

    public static Reservation create(String name, LocalDate date, ReservationTime reservationTime) {
        return Reservation.of(name, date, reservationTime);
    }

    public static ReservationReqDto createReqDto(String name, LocalDate date, Long timeId) {
        return new ReservationReqDto(name, date, timeId);
    }
}
