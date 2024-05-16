package roomescape.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class ReservationTerm {
    private final LocalDate startDate;
    private final LocalDate endDate;

    private ReservationTerm(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static ReservationTerm of(int term) {
        return new ReservationTerm(LocalDate.now().minusDays(term), LocalDate.now().minusDays(1));
    }

    public String getStartDate() {
        return startDate.format(DateTimeFormatter.ISO_DATE);
    }

    public String getEndDate() {
        return endDate.format(DateTimeFormatter.ISO_DATE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReservationTerm that = (ReservationTerm) o;
        return Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }
}
