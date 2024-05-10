package roomescape.dto;

import roomescape.exception.NullRequestParameterException;

import java.time.LocalTime;

public record ReservationTimeRequestDto(LocalTime startAt) {

    public ReservationTimeRequestDto {
        if (startAt == null) {
            throw new NullRequestParameterException("시간을 입력하여야 합니다.");
        }
    }
}
