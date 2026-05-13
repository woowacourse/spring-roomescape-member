package roomescape.controller.dto;

import roomescape.exception.InvalidInputException;

import java.time.LocalDate;

public record ReservationRequest(
        String name,
        LocalDate date,
        Long timeId,
        Long themeId) {

    public ReservationRequest {
        validateName(name);
        validateDate(date);
        validateTimeId(timeId);
        validateTheme(themeId);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("이름은 비어 있을 수 없습니다.");
        }
        if (name.length() > 255) {
            throw new InvalidInputException("이름은 255자를 넘을 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new InvalidInputException("날짜는 비어 있을 수 없습니다.");
        }
    }

    private void validateTimeId(Long timeId) {
        if (timeId == null || timeId <= 0) {
            throw new InvalidInputException("시간ID는 양수이어야 합니다.");
        }
    }

    private void validateTheme(Long themeId) {
        if (themeId == null || themeId <= 0) {
            throw new InvalidInputException("테마ID는 양수이어야 합니다.");
        }
    }
}
