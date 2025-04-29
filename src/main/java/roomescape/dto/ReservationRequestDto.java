package roomescape.dto;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public record ReservationRequestDto(
        String name,
        String rawDate,
        Long timeId
) {
    public Reservation convertToReservation() {
        try {
            LocalDate date = LocalDate.parse(rawDate);
            return new Reservation(
                    this.name,
                    date,
                    new ReservationTime(timeId)
            );
        } catch (DateTimeParseException e) {
            throw new IllegalStateException("날짜형식이 잘못되었습니다");
        }
    }
}
