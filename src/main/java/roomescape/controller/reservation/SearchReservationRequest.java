package roomescape.controller.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public record SearchReservationRequest(Long themeId, Long memberId, String dateFrom, String dateTo) {

    public boolean isValid() {
        return themeId != null && memberId != null && dateFrom != null && dateTo != null &&
                isValidDate(dateFrom) && isValidDate(dateTo);
    }

    private boolean isValidDate(String date) {
        try {
            LocalDate.parse(date);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }
}
