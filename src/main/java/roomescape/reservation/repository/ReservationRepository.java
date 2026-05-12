package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;

public interface ReservationRepository {

    void deleteById(long id);

    Reservation save(Reservation reservation);

    void update(Reservation reservation);

    boolean existsByDateAndTimeId(LocalDate date, long timeId);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);

    List<Long> findAllByDateAndThemeId(LocalDate date, long themeId);

    List<Reservation> findAll();

    List<Reservation> findAllByName(final String name);

    Optional<Reservation> findById(long id);

}
