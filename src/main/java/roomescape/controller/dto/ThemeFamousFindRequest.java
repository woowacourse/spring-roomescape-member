package roomescape.controller.dto;

import java.time.LocalDate;

public class ThemeFamousFindRequest {
    private static final int MIN_DAYS = 1;
    private static final int MIN_LIMIT = 1;
    private static final String DAYS_SHOULD_BE_POSITIVE = String.format("조회 기간은 %d일 이상이어야 합니다.", MIN_DAYS);
    private static final String DATE_SHOULD_NOT_BE_FUTURE = "기준 날짜는 미래일 수 없습니다.";
    private static final String LIMIT_SHOULD_BE_POSITIVE = String.format("최대 조회 개수는 %d건 이상이어야 합니다", MIN_LIMIT);

    private final Long days;
    private final LocalDate date;
    private final Long limit;

    public ThemeFamousFindRequest(Long days, LocalDate date, Long limit) {
        validate(days, date, limit);
        this.days = days;
        this.date = date;
        this.limit = limit;
    }

    public void validate(Long days, LocalDate date, Long limit) {
        validateDays(days);
        validateDate(date);
        validateLimit(limit);
    }

    private void validateLimit(Long limit) {
        if (limit == null) {
            return;
        }
        if (limit < MIN_LIMIT) {
            throw new IllegalArgumentException(LIMIT_SHOULD_BE_POSITIVE);
        }
    }

    private void validateDate(LocalDate date) {
        if (date == null) {
            return;
        }
        LocalDate now = LocalDate.now();
        if (now.isBefore(date)) {
            throw new IllegalArgumentException(DATE_SHOULD_NOT_BE_FUTURE);
        }
    }

    private void validateDays(Long days) {
        if (days == null) {
            return;
        }
        if (days < MIN_DAYS) {
            throw new IllegalArgumentException(DAYS_SHOULD_BE_POSITIVE);
        }
    }

    public Long getDays() {
        return days;
    }

    public LocalDate getDate() {
        return date;
    }

    public Long getLimit() {
        return limit;
    }
}


