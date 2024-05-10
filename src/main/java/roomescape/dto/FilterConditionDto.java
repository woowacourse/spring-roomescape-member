package roomescape.dto;

import java.time.LocalDate;

public record FilterConditionDto(Long themeId, Long memberId, LocalDate dateFrom, LocalDate dateTo) {
}
