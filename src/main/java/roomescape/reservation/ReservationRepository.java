package roomescape.reservation;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    List<Reservation> findAll();
    Reservation save(Reservation reservation);
    void delete(long id);
    int countByTimeId(long timeId);
    Optional<Reservation> findById(long id);
    boolean existsByDateTimeAndTheme(LocalDate date, Long timeId, Long themeId);
}
