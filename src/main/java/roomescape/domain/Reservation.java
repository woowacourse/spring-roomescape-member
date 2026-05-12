package roomescape.domain;

import lombok.Getter;
import roomescape.domain.dto.ReservationCreateData;

import java.time.LocalDate;

@Getter
public class Reservation {

    private final Long id;
    private final PersonName name;
    private final LocalDate date;
    private final ReservationTime time;
    private final Theme theme;

    private Reservation(final Long id, final PersonName name, final LocalDate date, final ReservationTime time, final Theme theme) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.time = time;
        this.theme = theme;
    }

    public static Reservation create(ReservationCreateData data) {
        return new Reservation(
                null,
                new PersonName(data.name()),
                data.date(),
                data.time(),
                data.theme()
        );
    }

    public static Reservation createWithId(
            final Long id,
            final String name,
            final LocalDate date,
            final ReservationTime time,
            final Theme theme) {
        return new Reservation(
                id,
                new PersonName(name),
                date,
                time,
                theme
        );
    }

    public Reservation withId(final Long id) {
        return new Reservation(
                id,
                name,
                date,
                time,
                theme
        );
    }

    public String getName() {
        return name.getName();
    }
}
