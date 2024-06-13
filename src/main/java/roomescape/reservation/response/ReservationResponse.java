package roomescape.reservation.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import roomescape.reservation.domain.Reservation;

public record ReservationResponse(long id, String memberName, String themeName, LocalDate date,
                                  @JsonFormat(pattern = "HH:mm") LocalTime startAt) {
    public static ReservationResponse from(Reservation reservation){
        return new ReservationResponse(
                reservation.id(),
                reservation.member().name(),
                reservation.theme().name(),
                reservation.date(),
                reservation.time().startAt()
        );
    }
}
