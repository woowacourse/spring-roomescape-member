package roomescape.domain.builder;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

import java.time.LocalDate;

public interface ReservationBuilder {
    ReservationBuilder id(Long id);

    ReservationBuilder name(String name);

    ReservationBuilder date(String date);

    ReservationBuilder date(LocalDate date);

    ReservationBuilder time(ReservationTime time);

    ReservationBuilder theme(Theme theme);

    Reservation build();
}
