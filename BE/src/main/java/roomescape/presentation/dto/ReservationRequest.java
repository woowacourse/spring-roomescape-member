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
    }

    private static void validateNameNotEmpty(String name) {
        if (name == null || name.trim().isBlank()) {
            throw new EntityNotFoundException("예약자의 이름이 비어있습니다.");
        }
    }
    
    private static void validateTimeIdNotEmpty(Long timeId) {
        if (timeId == null) {
            throw new EntityNotFoundException("예약 시간이 비어있습니다.");
        }
    }

    private static void validateDateNotEmpty(LocalDate date) {
        if (date == null) {
            throw new EntityNotFoundException("예약 날짜가 비어있습니다.");
        }
    }
}
