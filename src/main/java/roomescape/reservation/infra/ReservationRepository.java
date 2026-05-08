package roomescape.reservation.infra;

import roomescape.reservation.domain.Reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    void deleteById(long id);

    Set<Long> findTimeIdByDateAndThemeId(LocalDate date, long themeId);
}
