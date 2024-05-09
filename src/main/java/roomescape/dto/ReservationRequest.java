package roomescape.dto;

import java.time.LocalDate;

public record ReservationRequest(String name, LocalDate date, Long timeId, Long themeId) {

    public ReservationRequest {
        InputValidator.validateNotNull(name, date, timeId, themeId);
        InputValidator.validateNotBlank(name);
    }
}
