package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.Reservation;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    List<Reservation> findAll(int page, int size);

    Optional<Reservation> findById(long id);

    List<Reservation> findByUserName(String name);

    List<Reservation> findByThemeAndDate(long themeId, LocalDate date);

    void deleteById(long id);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);
}
