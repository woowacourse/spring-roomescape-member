package roomescape.domain;

import java.time.LocalDate;

public record Duration(
        LocalDate startDate,
        LocalDate endDate
) {
    public Duration {
        validateNotNull(startDate, endDate);
        validateDateOrder(startDate, endDate);
    }

    private void validateNotNull(LocalDate startDate, LocalDate endDate) {
        if (startDate == null) {
            throw new IllegalArgumentException("시작일이 필요합니다.");
        }
        if (endDate == null) {
            throw new IllegalArgumentException("종료일이 필요합니다.");
        }
    }

    private void validateDateOrder(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            throw new IllegalArgumentException("시작일은 종료일과 같거나 앞서야 합니다."
                    + " startDate = " + startDate
                    + " endDate = " + endDate
            );
        }
    }
}
