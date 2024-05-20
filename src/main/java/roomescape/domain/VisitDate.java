package roomescape.domain;

import java.time.DateTimeException;
import java.time.LocalDate;
import roomescape.exception.CustomBadRequest;

public record VisitDate(LocalDate date) {

    public static VisitDate from(final String date) {
        try {
            return new VisitDate(LocalDate.parse(date));
        } catch (final DateTimeException exception) {
            throw new CustomBadRequest(String.format("date(%s)가 유효하지 않습니다.", date));
        }
    }

    public VisitDate beforeDay() {
        return new VisitDate(this.date.minusDays(1));
    }

    public VisitDate beforeWeek() {
        return new VisitDate(this.date.minusWeeks(1));
    }

    public String asString() {
        return date.toString();
    }
}
