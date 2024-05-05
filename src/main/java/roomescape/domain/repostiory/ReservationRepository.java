package roomescape.domain.repostiory;

import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.InvalidReservationException;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(long id);

    boolean existsByDateAndTimeAndTheme(String date, long timeId, long themeId);

    boolean existsByTimeId(long id);

    boolean existsByThemeId(long id);
}
