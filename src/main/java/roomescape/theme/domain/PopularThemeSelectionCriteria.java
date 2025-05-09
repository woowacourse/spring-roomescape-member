package roomescape.theme.domain;

import java.time.LocalDate;
import roomescape.exception.custom.InvalidInputException;

public class PopularThemeSelectionCriteria {

    private static final int DAY_BEFORE_END = 1;

    private final LocalDate startDay;
    private final LocalDate endDay;

    public PopularThemeSelectionCriteria(final LocalDate startBaseDate, final int durationInDays) {
        validateInvalidInput(startBaseDate, durationInDays);
        this.startDay = calculateDate(startBaseDate, durationInDays + DAY_BEFORE_END);
        this.endDay = calculateDate(startBaseDate, DAY_BEFORE_END);
    }

    private void validateInvalidInput(final LocalDate startBaseDate, final int durationInDays) {
        if (startBaseDate == null) {
            throw new InvalidInputException("시작 기준일은 null이 될 수 없습니다");
        }
        if (durationInDays <= 0) {
            throw new InvalidInputException("인기 테마 선정 기준 기한 일수는 음수나 0이 될 수 없습니다");
        }
    }

    private LocalDate calculateDate(final LocalDate baseDate, final int daysBefore) {
        return baseDate.minusDays(daysBefore);
    }

    public LocalDate getStartDay() {
        return startDay;
    }

    public LocalDate getEndDay() {
        return endDay;
    }
}
