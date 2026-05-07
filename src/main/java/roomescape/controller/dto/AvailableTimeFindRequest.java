package roomescape.controller.dto;

import java.time.LocalDate;

public class AvailableTimeFindRequest {
    private static final String DATE_SHOULD_NOT_BE_NULL = "날짜는 필수로 입력해야 합니다.";
    private static final String THEME_ID_SHOULD_NOT_BE_NULL = "테마 아이디는 필수로 입력해야 합니다.";
    private static final String THEME_ID_SHOULD_BE_POSITIVE = "테마 아이디는 양수여야 합니다.";
    private static final int MIN_THEME_ID = 1;

    private final LocalDate date;
    private final Long themeId;

    public AvailableTimeFindRequest(LocalDate date, Long themeId) {
        validate(date, themeId);
        this.date = date;
        this.themeId = themeId;
    }

    private void validate(LocalDate date, Long themeId) {
        validateDate(date);
        validateTheme(themeId);
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException(DATE_SHOULD_NOT_BE_NULL);
        }
    }

    private void validateTheme(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException(THEME_ID_SHOULD_NOT_BE_NULL);
        }
        if (themeId < MIN_THEME_ID) {
            throw new IllegalArgumentException(THEME_ID_SHOULD_BE_POSITIVE);
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getThemeId() {
        return themeId;
    }
}
