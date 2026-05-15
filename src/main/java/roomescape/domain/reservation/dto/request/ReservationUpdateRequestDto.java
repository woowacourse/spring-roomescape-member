package roomescape.domain.reservation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationUpdateRequestDto(@NotNull @JsonFormat(pattern = "yyyy-MM-dd") LocalDate date,
                                          @NotNull Long timeId) {

}
