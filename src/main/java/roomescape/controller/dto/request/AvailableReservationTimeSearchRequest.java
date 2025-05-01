package roomescape.controller.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AvailableReservationTimeSearchRequest(
        @NotNull LocalDate date,
        @NotNull long themeId
) {
}
