package roomescape.service.dto;

import jakarta.validation.constraints.NotNull;

public record ReservationTimeRequest(@NotNull String startAt) {
}
