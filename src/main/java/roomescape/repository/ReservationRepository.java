package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.model.Reservation;

public interface ReservationRepository {

    Optional<Reservation> findById(long id);

    long save(Reservation reservation);

    boolean removeById(long id);

    List<Reservation> findAll();

    List<Reservation> findByTimeSlotId(long id);

    List<Reservation> findByThemeId(long id);

    List<Reservation> findByDateAndThemeId(LocalDate date, long themeId);
}
