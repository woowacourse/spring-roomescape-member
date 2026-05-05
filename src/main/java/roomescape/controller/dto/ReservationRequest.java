package roomescape.controller.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
            throw new IllegalArgumentException("[ERROR] 이름은 비어 있을 수 없습니다.");
        }
        if (name.length() > 255) {
            throw new IllegalArgumentException("[ERROR] 이름은 255자를 넘을 수 없습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜는 비어 있을 수 없습니다.");
        }
    }

    private void validateTimeId(Long timeId) {
        if (timeId == null || timeId <= 0) {
            throw new IllegalArgumentException("[ERROR] 시간ID는 양수이어야 합니다.");
        }
    }

    private void validateTheme(Long themeId) {
        if (themeId == null || themeId <= 0) {
            throw new IllegalArgumentException("[ERROR] 테마ID는 양수이어야 합니다.");
        }
    }
}
