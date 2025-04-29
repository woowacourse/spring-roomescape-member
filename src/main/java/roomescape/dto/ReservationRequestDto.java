package roomescape.dto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public record ReservationRequestDto(
        String name,
        String date,
        Long timeId
) {
    public Reservation convertToReservation(ReservationTime reservationTime) {
        try {
            LocalDate parsedDate = LocalDate.parse(date);
            return new Reservation(
                    this.name,
                    parsedDate,
                    reservationTime,
                    null
            );
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("날짜형식이 잘못되었습니다");
        }
    }
}
