package roomescape.dto;

import java.time.LocalDate;

public record UserReservationRequest(LocalDate date, Long themeId, Long timeId) {
}
