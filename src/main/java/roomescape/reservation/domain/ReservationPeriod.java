package roomescape.reservation.domain;

import java.time.LocalDate;

public class ReservationPeriod {

    private final LocalDate standardDate;
    private final int startOffset;
    private final int endOffset;

    public ReservationPeriod(LocalDate standardDate, int startOffset, int endOffset) {
        this.standardDate = standardDate;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
    }

    public LocalDate findStartDate() {
        return standardDate.minusDays(startOffset);
    }

    public LocalDate findEndDate() {
        return standardDate.minusDays(endOffset);
    }
}
