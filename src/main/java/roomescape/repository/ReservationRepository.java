package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    boolean existsByThemeAndDateAndTime(Theme theme, LocalDate date, ReservationTime reservationTime);

    boolean existsByTime(ReservationTime reservationTime);

    boolean existsByTheme(Theme theme);

    void delete(long id);
}
