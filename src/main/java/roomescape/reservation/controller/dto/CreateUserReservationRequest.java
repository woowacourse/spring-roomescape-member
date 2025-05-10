package roomescape.reservation.controller.dto;

import java.time.LocalDate;

public record CreateUserReservationRequest(LocalDate date, long timeId, long themeId) {
}
