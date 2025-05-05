package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.model.Reservation;

public interface ReservationRepository {

    Optional<Reservation> findById(final long id);

    long save(final Reservation reservation);

    boolean removeById(final long id);

    List<Reservation> findAll();

    List<Reservation> findByTimeSlotId(final long id);

    List<Reservation> findByThemeId(final long id);

    List<Reservation> findByDateAndThemeId(final LocalDate date, final long themeId);

    Optional<Reservation> findByDateAndTimeSlotAndThemeId(LocalDate date, long timeSlotId, long themeId);
}
