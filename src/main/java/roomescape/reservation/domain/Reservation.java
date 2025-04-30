package roomescape.reservation.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Getter
@AllArgsConstructor(onConstructor_ = @__(@JsonCreator))
public class Reservation {
    private final Long id;

    @NonNull
    private final String name;

    @NonNull
    private final LocalDate date;

    @NonNull
    private final ReservationTime time;

    @NonNull
    private final Theme theme;

    public Reservation(final String name, final LocalDate date, final ReservationTime reservationTime,
                       final Theme theme) {
        this.id = null;
        this.name = name;
        this.date = date;
        this.time = reservationTime;
        this.theme = theme;
    }

    public LocalTime extractTime() {
        return time.getStartAt();
    }
}
