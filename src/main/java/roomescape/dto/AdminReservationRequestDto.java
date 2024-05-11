package roomescape.dto;

import roomescape.exception.InvalidParameterException;

import java.time.LocalDate;

public record AdminReservationRequestDto(LocalDate date, Long timeId, Long themeId, Long memberId) {

    public AdminReservationRequestDto {
        if (date == null) {
            throw new InvalidParameterException("날짜를 입력하여야 합니다.");
        }
        if (timeId == null) {
            throw new InvalidParameterException("시간을 입력하여야 합니다.");
        }
        if (themeId == null) {
            throw new InvalidParameterException("테마를 입력하여야 합니다.");
        }
        if (memberId == null) {
            throw new InvalidParameterException("멤버를 입력하여야 합니다.");
        }
    }
}
