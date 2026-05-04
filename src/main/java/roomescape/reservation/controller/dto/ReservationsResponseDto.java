package roomescape.reservation.controller.dto;

import java.util.List;

public class ReservationsResponseDto {
    private final List<ReservationResponseDto> reservations;

    public ReservationsResponseDto(List<ReservationResponseDto> reservations) {
        this.reservations = reservations;
    }

    public List<ReservationResponseDto> getReservations() {
        return reservations;
    }
}
