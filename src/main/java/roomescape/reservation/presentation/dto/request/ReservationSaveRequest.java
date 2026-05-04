package roomescape.reservation.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationSaveRequest(@NotBlank String name,
                                     @JsonFormat(pattern = "yyyy-MM-dd") @NotNull LocalDate date,
                                     @NotNull Long timeId) {
}
