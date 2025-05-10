package roomescape.reservation.controller.dto;

import java.time.LocalDate;

public record CreateAdminReservationRequest(LocalDate date, long timeId, long themeId, long memberId) {
}

