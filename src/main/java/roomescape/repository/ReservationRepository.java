package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;

public interface ReservationRepository {

    List<Reservation> findAll();

    boolean existsByDateAndTimeAndTheme(final LocalDate date, final LocalTime time, final Long themeId);

    boolean existsByReservationTimeId(final Long id);

    boolean existsByThemeId(Long id);

    Reservation save(final Reservation reservation);

    Optional<Reservation> findById(final Long id);

    void deleteById(final Long id);
}
