package roomescape.domain.reservation.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUserCreateRequest(@FutureOrPresent @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                           @NotNull Long timeId, @NotNull Long themeId) {

}
