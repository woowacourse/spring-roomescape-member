package roomescape.controller.dto;

import java.time.LocalDate;

public record AvailableTimeCondition(LocalDate date, long themeId) {
}
