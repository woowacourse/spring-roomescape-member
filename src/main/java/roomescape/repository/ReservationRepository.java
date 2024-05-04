package roomescape.repository;

import java.time.LocalDate;
import roomescape.domain.Reservation;
import roomescape.domain.Reservations;
import roomescape.domain.Theme;
import roomescape.domain.Themes;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Reservations findAll();

    Reservations findByThemeAndDate(Theme theme, LocalDate date);

    Themes findAndOrderByPopularity(LocalDate start, LocalDate end, int count);

    boolean existByTimeId(long timeId);

    boolean existByThemeId(long themeId);

    void delete(long id);
}
