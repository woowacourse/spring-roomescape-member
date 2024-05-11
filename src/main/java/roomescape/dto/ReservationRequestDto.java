package roomescape.dto;

import roomescape.exception.InvalidParameterException;

import java.time.LocalDate;

public record ReservationRequestDto(LocalDate date, Long timeId, Long themeId) {

    public ReservationRequestDto {
        if (date == null) {
            throw new InvalidParameterException("날짜를 입력하여야 합니다.");
        }
        if (timeId == null) {
            throw new InvalidParameterException("시간을 입력하여야 합니다.");
        }
        if (themeId == null) {
            throw new InvalidParameterException("테마를 입력하여야 합니다.");
        }
    }
}
