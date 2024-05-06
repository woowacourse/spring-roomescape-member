package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.reservation.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    boolean existsByReservationDateTimeAndTheme(LocalDate date, long timeId, long themeId);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    List<Reservation> findAll();

    int deleteById(long id);
}
