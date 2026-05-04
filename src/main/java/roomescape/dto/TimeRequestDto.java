package roomescape.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TimeRequestDto(
        @NotNull LocalTime startAt
) {
}
