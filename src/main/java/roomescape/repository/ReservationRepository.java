package roomescape.repository;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.Reservations;
import roomescape.domain.Theme;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Reservations findAll();

    Reservations findByThemeAndDate(Theme theme, LocalDate date);

    boolean existByTimeId(long timeId);

    boolean existByThemeId(long themeId);

    void delete(long id);
}
