package roomescape.dto;

import java.time.LocalDate;

public final class ReservationCreationRequest {

    private final String name;
    private final LocalDate date;
    private final Long timeId;
    private final Long themeId;

    public ReservationCreationRequest(final String name, final LocalDate date, final Long timeId, final Long themeId) {
        validate(name, date, timeId, themeId);
        this.name = name;
        this.date = date;
        this.timeId = timeId;
        this.themeId = themeId;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getTimeId() {
        return timeId;
    }

    public Long getThemeId() {
        return themeId;
    }

    private void validate(String name, LocalDate date, Long timeId, Long themeId) {
        validateName(name);
        validateDate(date);
        validateTime(timeId);
        validateTheme(themeId);
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("[ERROR] 이름은 빈 값이나 공백값을 허용하지 않습니다.");
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            throw new IllegalArgumentException("[ERROR] 날짜는 빈 값을 허용하지 않습니다.");
        }
    }

    private void validateTime(Long timeId) {
        if (timeId == null) {
            throw new IllegalArgumentException("[ERROR] 시간은 빈 값을 허용하지 않습니다.");
        }
    }

    private void validateTheme(Long themeId) {
        if (themeId == null) {
            throw new IllegalArgumentException("[ERROR] 테마는 빈 값을 허용하지 않습니다.");
        }
    }
}
