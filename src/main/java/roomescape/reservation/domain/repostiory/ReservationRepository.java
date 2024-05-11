package roomescape.reservation.domain.repostiory;

import roomescape.reservation.domain.Reservation;

import java.util.List;

public interface ReservationRepository {
    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(long id);

    boolean existsByDateAndTimeAndTheme(String date, long timeId, long themeId);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    boolean existsById(long id);
}
