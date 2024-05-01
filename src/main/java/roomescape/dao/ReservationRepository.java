package roomescape.dao;

import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(long id);

    boolean existsByDateAndTimeAndTheme(String date, long timeId, long themeId);

    boolean existsByTimeId(long id);
}
