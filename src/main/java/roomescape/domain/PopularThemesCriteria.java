package roomescape.domain;

import java.time.LocalDate;

public class PopularThemesCriteria {

    private static final int START_DATE = 7;
    private static final int END_DATE = 1;
    private static final int COUNT_LIMIT = 10;

    public LocalDate getStartDate() {
        return LocalDate.now().minusDays(START_DATE);
    }

    public LocalDate getEndDate() {
        return LocalDate.now().minusDays(END_DATE);
    }

    public int getCountLimit() {
        return COUNT_LIMIT;
    }
}
