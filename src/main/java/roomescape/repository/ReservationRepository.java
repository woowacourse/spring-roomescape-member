package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.Reservations;
import roomescape.domain.Theme;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Reservations findAll();

    Reservations findByThemeAndDate(Theme theme, LocalDate date);

    void delete(long id);
}
