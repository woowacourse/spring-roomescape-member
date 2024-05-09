package roomescape.domain;

import java.time.DateTimeException;
import java.time.LocalDate;
import roomescape.exception.InvalidInputException;

public record VisitDate(LocalDate date) {

    public static VisitDate from(final String date) {
        try {
            return new VisitDate(LocalDate.parse(date));
        } catch (final DateTimeException exception) {
            throw InvalidInputException.of("date", date);
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
