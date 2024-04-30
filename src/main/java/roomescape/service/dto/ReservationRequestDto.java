package roomescape.service.dto;

import java.time.LocalTime;
import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;

public class ReservationRequestDto {

    private final String name;
    private final String date;
    private final long timeId;

    public ReservationRequestDto(String name, String date, long timeId) {
        this.name = name;
        this.date = date;
        this.timeId = timeId;
    }

    public Reservation toReservation() {
        return new Reservation(null, new Name(name), new ReservationDate(date), new ReservationTime(timeId,
                (LocalTime) null));
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public long getTimeId() {
        return timeId;
    }
}
