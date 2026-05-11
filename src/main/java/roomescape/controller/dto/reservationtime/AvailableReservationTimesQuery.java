package roomescape.controller.dto.reservationtime;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.global.exception.InvalidReservationTimeException;

public record AvailableReservationTimesQuery(
        Long themeId,
        LocalDate date,
        Boolean available
) {

    public static AvailableReservationTimesQuery toQuery(Long themeId, String date, Boolean available) {
        if (themeId == null) {
            throw new InvalidReservationTimeException("테마 ID는 필수입니다.");
        }
        return new AvailableReservationTimesQuery(themeId, parseDate(date), available);
    }

    private static LocalDate parseDate(String date) {
        if (date == null || date.isBlank()) {
            throw new InvalidReservationTimeException("예약 날짜는 필수입니다.");
        }

        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new InvalidReservationTimeException("예약 날짜 형식은 yyyy-MM-dd 이어야 합니다.");
        }
    }
}
