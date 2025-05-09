package roomescape.reservation.controller.dto;

import java.time.LocalDate;

public record CreateReservationRequest(LocalDate date, long timeId, long themeId) {
}
