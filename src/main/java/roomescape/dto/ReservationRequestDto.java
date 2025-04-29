package roomescape.dto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

public record ReservationRequestDto(
        String name,
        String date,
        Long timeId,
        Long themeId
) {
    public Reservation convertToReservation(ReservationTime reservationTime, Theme theme) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            return new Reservation(
                    this.name,
                    parsedDate,
                    reservationTime,
                    theme
            );
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("날짜형식이 잘못되었습니다");
        }
    }
}
