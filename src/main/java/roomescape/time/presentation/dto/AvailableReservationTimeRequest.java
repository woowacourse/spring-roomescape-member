package roomescape.time.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import roomescape.time.application.dto.AvailableReservationTimeFindCommand;

public record AvailableReservationTimeRequest(
        @NotNull
        Long themeId,
        @NotNull
        LocalDate date
) {
    public AvailableReservationTimeFindCommand toCommand() {
        return AvailableReservationTimeFindCommand.builder()
                .themeId(this.themeId)
                .date(this.date)
                .build();
    }
}
