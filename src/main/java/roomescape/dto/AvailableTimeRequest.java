package roomescape.dto;

import java.time.LocalDate;

public record AvailableTimeRequest(LocalDate date, Long themeId) {
}
