package roomescape.reservation.dto;

import java.util.List;

public record PageReservationsResponse(
        List<ReservationResponse> reservations,
        int page,
        int size,
        boolean hasNext
) {
    public static PageReservationsResponse from(List<ReservationResponse> reservations, int page, int size,
                                                boolean hasNext) {
        return new PageReservationsResponse(reservations, page, size, hasNext);
    }
}
