package roomescape.domain.builder;

import roomescape.domain.Name;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public class ReservationBuilderImpl implements ReservationBuilder {
    private Long id;
    private String name;
    private LocalDate date;
    private ReservationTime reservationTime;
    private Theme reservationTheme;

    @Override
    public ReservationBuilder id(final Long id) {
        this.id = id;
        return this;
    }

    @Override
    public ReservationBuilder name(final String name) {
        this.name = name;
        return this;
    }

    @Override
    public ReservationBuilder date(final String date) {
        this.date = LocalDate.parse(date);
        return this;
    }

    @Override
    public ReservationBuilder date(LocalDate date) {
        this.date = date;
        return this;
    }

    @Override
    public ReservationBuilder time(final ReservationTime time) {
        this.reservationTime = time;
        return this;
    }

    @Override
    public ReservationBuilder theme(final Theme theme) {
        this.reservationTheme = theme;
        return this;
    }

    @Override
    public Reservation build() {
        return new Reservation(
                id,
                new Name(name),
                date,
                reservationTime,
                reservationTheme
        );
    }
}
