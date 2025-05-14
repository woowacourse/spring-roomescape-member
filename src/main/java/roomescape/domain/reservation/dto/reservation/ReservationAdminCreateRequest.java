package roomescape.domain.reservation.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationAdminCreateRequest(@NotNull Long memberId, @NotNull Long timeId, @NotNull Long themeId,
                                            @FutureOrPresent @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date) {
}
