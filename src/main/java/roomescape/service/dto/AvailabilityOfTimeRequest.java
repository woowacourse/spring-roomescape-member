package roomescape.service.dto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AvailabilityOfTimeRequest {

    private final String date;
    private final Long themeId;

    public AvailabilityOfTimeRequest(String date, Long themeId) {
        validateDateExist(date);
        validateDateFormat(date);
        validateThemeIdExist(themeId);
        validateIdNaturalNumber(themeId);
        this.date = date;
        this.themeId = themeId;
    }

    private void validateDateExist(String date) {
        if (date == null || date.isBlank()) {
            throw new IllegalArgumentException("날짜는 반드시 입력되어야 합니다.");
        }
    }

    private void validateDateFormat(String date) {
        try {
            LocalDate.parse(date);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("날짜 형식이 올바르지 않습니다.");
        }
    }

    private void validateThemeIdExist(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("테마 아이디는 반드시 입력되어야 합니다.");
        }
    }

    private void validateIdNaturalNumber(Long id) {
        if (id <= 0) {
            throw new IllegalArgumentException("아이디는 자연수여야 합니다.");
        }
    }

    public String getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }
}
