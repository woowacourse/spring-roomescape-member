package roomescape.dto;

import java.time.LocalDate;
import java.util.List;
import roomescape.entity.Reservation;

public record ReservationResponse(Long id,
                                  String name,
                                  LocalDate date,
                                  ReservationTimeResponse time) {

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getName(),
                reservation.getDate(),
                ReservationTimeResponse.from(reservation.getTime())
        );
    }

    public static ReservationResponse of(ReservationResponse reservationResponse,
                                         ReservationTimeResponse reservationTimeResponse) {
        return new ReservationResponse(
                reservationResponse.id(),
                reservationResponse.name(),
                reservationResponse.date(),
                reservationTimeResponse);
    }

    public static List<ReservationResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

}
