package roomescape.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TimeRequestDto(
        @NotNull(message = "시간을 입력해 주세요.")
        LocalTime startAt
) {
}
