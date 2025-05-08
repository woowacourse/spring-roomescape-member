package roomescape.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record ReservationRequestDto(String name, String date, Long timeId, Long themeId) {

    public ReservationRequestDto {
        validateName(name);
        validateDate(date);
        validateTimeId(timeId);
    }

    private void validateName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("잘못된 이름입니다.");
        }
    }

    private void validateDate(String date) {
        if (date == null || date.isEmpty()) {
            throw new IllegalArgumentException("잘못된 날짜입니다.");
        }

        validateParse(date);
    }

    private void validateParse(String date) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            LocalDate.parse(date, dateTimeFormatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("잘못된 날짜 형식입니다.");
        }
    }

    private void validateTimeId(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("잘못된 시간ID입니다.");
        }
    }
}
