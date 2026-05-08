package roomescape.dto.reservation;

import jakarta.validation.constraints.NotBlank;

public record ReservationCondition(
        String name
) {
}
