package roomescape.reservation.infra;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    List<Reservation> findByName(String name);

    void deleteById(Long id);

    List<Long> findTimeIdByDateAndThemeId(LocalDate date, long themeId);
}
