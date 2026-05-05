package roomescape.domain;

import java.time.LocalDate;

public record Duration(
        LocalDate startDate,
        LocalDate endDate
) {
    public Duration {
        validateDates(startDate, endDate);
    }

    private void validateDates(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return;
        }

        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("시작일은 종료일과 같거나 앞서야 합니다."
                    + " startDate = " + startDate
                    + " endDate = " + endDate
            );
        }
    }
}
