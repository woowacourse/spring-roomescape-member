package roomescape.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import java.time.LocalDate;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;

public record ReservationSaveRequest(
        String name,
        @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
        LocalDate date,
        Long timeId
) {

    public ReservationSaveRequest(final String name, final String date, final Long timeId) {
        this(name, LocalDate.parse(date), timeId);
    }

    public Reservation toReservation(final ReservationTime reservationTime) {
        return new Reservation(name, date, reservationTime);
    }
}
