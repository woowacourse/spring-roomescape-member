package roomescape.reservation.application.dto;

import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationName;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.domain.Theme;

public class CreateReservationRequest {
    private final ReservationName name;
    private final Theme theme;
    private final ReservationDate date;
    private final ReservationTime time;

    public CreateReservationRequest(ReservationName name, Theme theme, ReservationDate date, ReservationTime time) {
        this.name = name;
        this.theme = theme;
        this.date = date;
        this.time = time;
    }

    public ReservationName getName() {
        return name;
    }

    public ReservationDate getDate() {
        return date;
    }

    public ReservationTime getTime() {
        return time;
    }

    public Theme getTheme() {
        return theme;
    }
}
