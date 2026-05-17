package roomescape.reservation.infra;

import java.time.LocalDate;
import java.util.List;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    java.util.Optional<Reservation> findById(Long id);

    List<Reservation> findByName(String name);

    void update(Reservation reservation);

    void deleteById(Long id);

    void deleteByName(String name);

    List<Long> findTimeIdByDateAndThemeId(LocalDate date, long themeId);
}
