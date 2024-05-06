package roomescape.service.dto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PopularThemeRequestDto {

    private final String startDate;
    private final String endDate;
    private final Integer count;

    public PopularThemeRequestDto(String startDate, String endDate, Integer count) {
        validateDateExist(startDate);
        validateDateFormat(startDate);
        validateDateExist(endDate);
        validateDateFormat(endDate);
        validateLimitExist(count);
        validateLimitNaturalNumber(count);
        this.startDate = startDate;
        this.endDate = endDate;
        this.count = count;
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

    private void validateLimitExist(Integer themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("불러올 테마 개수는 반드시 입력되어야 합니다.");
        }
    }

    private void validateLimitNaturalNumber(Integer id) {
        if (id <= 0) {
            throw new IllegalArgumentException("불러올 테마 개수는 자연수여야 합니다.");
        }
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public Integer getCount() {
        return count;
    }
}
