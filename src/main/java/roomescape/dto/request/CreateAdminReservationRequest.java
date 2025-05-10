package roomescape.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;

public record CreateAdminReservationRequest(
        @NotNull
        Long memberId,
        @NotNull
        LocalDate date,
        @NotNull
        Long timeId,
        @NotNull
        Long themeId
) {
}
