package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.reservation.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    Optional<Reservation> findById(long id);

    void deleteById(long id);

    Reservation save(Reservation reservation);

    boolean existsByDateAndThemeIdAndTimeId(LocalDate date, long themeId, long timeId);

    List<Long> findReservedTimeIdsByDateAndThemeId(LocalDate date, long themeId);

    boolean existsByTimeId(long timeId);

    boolean existsByThemeId(long themeId);
}
