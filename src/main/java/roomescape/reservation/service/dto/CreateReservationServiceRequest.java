package roomescape.reservation.service.dto;

import java.time.LocalDate;

public record CreateReservationServiceRequest(LocalDate date, long timeId, long themeId, long memberId) {
}
