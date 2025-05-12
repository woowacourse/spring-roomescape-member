package roomescape.dto.admin;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record AdminReservationRequest(@NotNull LocalDate date, @NotNull Long themeId, @NotNull Long timeId,
                                      @NotNull Long memberId) {

}
