package roomescape.controller.request;

import java.time.LocalDate;

public record AdminReservationRequest(LocalDate date, long themeId, long timeId, long memberId) {
}
