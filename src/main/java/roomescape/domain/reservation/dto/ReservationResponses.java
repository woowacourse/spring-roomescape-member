package roomescape.domain.reservation.dto;

import roomescape.domain.reservation.domain.Reservation;

import java.util.List;

public record ReservationResponses(
        List<ReservationResponse> reservations,
        long totalCount,
        int page,
        int size
) {
    public static ReservationResponses from(List<Reservation> reservations, long totalCount, int page, int size) {
        return new ReservationResponses(
                reservations.stream().map(ReservationResponse::from).toList(),
                totalCount,
                page,
                size
        );
    }
}
