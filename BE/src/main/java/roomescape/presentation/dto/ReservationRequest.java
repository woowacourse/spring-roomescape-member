package roomescape.presentation.dto;

import java.time.LocalDate;
import roomescape.global.exception.EntityNotFoundException;

public record ReservationRequest(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId
) {
    public ReservationRequest{
        validateNameNotEmpty(name);
        validateDateNotEmpty(date);
        validateTimeIdNotEmpty(timeId);
        validateThemeIdNotEmpty(themeId);
    }

    private static void validateNameNotEmpty(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new EntityNotFoundException("예약자 이름을 입력해 주세요.");
        }
    }
    
    private static void validateTimeIdNotEmpty(Long timeId) {
        if (timeId == null) {
            throw new EntityNotFoundException("예약 시간을 선택해 주세요.");
        }
    }

    private static void validateThemeIdNotEmpty(Long themeId) {
        if (themeId == null) {
            throw new EntityNotFoundException("예약 테마를 선택해 주세요.");
        }
    }

    private static void validateDateNotEmpty(LocalDate date) {
        if (date == null) {
            throw new EntityNotFoundException("예약 날짜를 선택해 주세요.");
        }
    }
}
