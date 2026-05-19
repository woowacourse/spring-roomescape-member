package roomescape.domain;

import java.time.LocalDate;
import roomescape.exception.ErrorCode;
import roomescape.exception.InvalidDomainStateException;

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
            throw new InvalidDomainStateException(
                    ErrorCode.INVALID_DURATION,
                    "시작일이 필요합니다."
            );
        }
        if (endDate == null) {
            throw new InvalidDomainStateException(
                    ErrorCode.INVALID_DURATION,
                    "종료일이 필요합니다."
            );
        }
    }

    private void validateDateOrder(LocalDate startDate, LocalDate endDate) {
        if (endDate.isBefore(startDate)) {
            String message = "시작일은 종료일과 같거나 앞서야 합니다."
                    + " startDate = " + startDate
                    + " endDate = " + endDate;

            throw new InvalidDomainStateException(ErrorCode.INVALID_DURATION, message);
        }
    }
}
