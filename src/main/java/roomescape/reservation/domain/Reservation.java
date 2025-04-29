package roomescape.reservation.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import roomescape.reservationtime.domain.ReservationTime;

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

    public Reservation(final String name, final LocalDate date, final ReservationTime reservationTime) {
        this.id = null;
        this.name = name;
        this.date = date;
        this.time = reservationTime;
    }
}
