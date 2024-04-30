package roomescape.service.dto;

import roomescape.domain.Reservation;

import java.time.LocalDate;
import java.util.List;

public record ReservationResponse(Long id, String name, LocalDate date,
                                  ReservationTimeResponse time) {

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(reservation.getId(), reservation.getName(),
                reservation.getDate(), ReservationTimeResponse.of(reservation.getReservationTime()));
    }

    public static List<ReservationResponse> listOf(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::of)
                .toList();
    }
}
