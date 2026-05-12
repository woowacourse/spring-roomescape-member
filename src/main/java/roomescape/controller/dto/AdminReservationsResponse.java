package roomescape.controller.dto;

import java.util.List;

public record AdminReservationsResponse(List<AdminReservationResponse> reservations) {
}