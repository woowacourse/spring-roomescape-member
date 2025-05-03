package roomescape.dto.reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.common.exception.InvalidInputException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

public record ReservationRequestDto(
        String name,
        String date,
        Long timeId,
        Long themeId
) {
    public ReservationRequestDto {
        validateRequiredFields(name, date, timeId, themeId);
    }

    public Reservation convertToReservation(ReservationTime reservationTime, Theme theme) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            return new Reservation(name, parsedDate, reservationTime, theme);
        } catch (DateTimeParseException e) {
            throw new InvalidInputException("날짜 형식이 잘못되었습니다");
        }
    }

    private void validateRequiredFields(String name, String date, Long timeId, Long themeId) {
        if (name == null || name.isBlank()) {
            throw new InvalidInputException("예약자명은 null이거나 공백일 수 없습니다");
        }

        if (date == null || date.isBlank()) {
            throw new InvalidInputException("예약 날짜는 null이거나 공백일 수 없습니다");
        }

        if (timeId == null) {
            throw new InvalidInputException("예약 시각은 null 일 수 없습니다.");
        }

        if (themeId == null) {
            throw new InvalidInputException("테마는 null 일 수 없습니다.");
        }
    }
}
