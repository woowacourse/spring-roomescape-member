package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;

public record CancelReservationRequest(
        @NotBlank String name
) {
}