package roomescape.domain.reservation.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ReservationRequest(@NotNull String name, @NotNull LocalDate date,@NotNull Long timeId) {

}
