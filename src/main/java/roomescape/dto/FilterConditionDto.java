package roomescape.dto;

import roomescape.exception.InvalidParameterException;

import java.time.LocalDate;

public record FilterConditionDto(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {

    public FilterConditionDto {
        if (themeId == null) {
            throw new InvalidParameterException("테마를 입력하여야 합니다.");
        }
        if (memberId == null) {
            throw new InvalidParameterException("멤버를 입력하여야 합니다.");
        }
        if (dateFrom == null) {
            throw new InvalidParameterException("시작 날짜를을 입력하여야 합니다.");
        }
        if (dateTo == null) {
            throw new InvalidParameterException("종료 날짜를 입력하여야 합니다.");
        }
    }
}
