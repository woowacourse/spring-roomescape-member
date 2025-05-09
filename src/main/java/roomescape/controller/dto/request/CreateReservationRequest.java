package roomescape.controller.dto.request;

import java.time.LocalDate;

public record CreateReservationRequest(LocalDate date, long timeId, long themeId) {
}
