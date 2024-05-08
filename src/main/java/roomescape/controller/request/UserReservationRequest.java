package roomescape.controller.request;

import java.time.LocalDate;

public record UserReservationRequest(LocalDate date, Long themeId, Long timeId) {
}
