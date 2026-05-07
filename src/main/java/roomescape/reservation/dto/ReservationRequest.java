package roomescape.reservation.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReservationRequest(
        @NotBlank String name,
        @NotNull LocalDate date,
        @NotNull Long timeId
) {

}
