package roomescape.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(Long id);

    boolean existsById(Long id);

    boolean existsByTimeId(Long id);

    boolean existsByThemeId(Long id);

    boolean existsByReservation(LocalDate date, Long timeId, Long themeId);

    List<Reservation> findByDateAndThemeId(LocalDate date, Long themeId);
}
