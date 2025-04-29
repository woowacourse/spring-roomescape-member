package roomescape.reservationtime.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;

public record CreateTimeRequest(@NotNull LocalTime startAt) {
}
