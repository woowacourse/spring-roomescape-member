package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    Optional<Reservation> save(final Reservation reservation);

    List<Reservation> findAll();

    Optional<Reservation> findById(final Long id);

    List<Reservation> findByDateTime(final LocalDate date, final LocalTime time);

    List<Reservation> findByDateAndTheme(final LocalDate date, final long themeId);

    int deleteById(final long id);
}
