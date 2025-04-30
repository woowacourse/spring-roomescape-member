package roomescape.dto.response;

import java.time.LocalDate;
import roomescape.domain.Reservation;

public record ReservationCreateResponse(
        long id,
        String name,
        LocalDate date,
        ReservationTimeCreateResponse time,
        ThemeCreateResponse theme
) {

    public static ReservationCreateResponse from(Reservation reservation) {
        return new ReservationCreateResponse(reservation.getId(), reservation.getName(), reservation.getDate(),
                ReservationTimeCreateResponse.from(reservation.getTime()),
                ThemeCreateResponse.from(reservation.getTheme()));
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getDate() {
        return date;
    }

    public ReservationTimeCreateResponse getTime() {
        return time;
    }
}
