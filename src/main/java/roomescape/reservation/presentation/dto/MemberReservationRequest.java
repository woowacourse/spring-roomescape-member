package roomescape.reservation.presentation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record MemberReservationRequest(@NotNull LocalDate date,
                                       @NotNull Long themeId,
                                       @NotNull Long timeId) {
}
