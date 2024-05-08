package roomescape.core.repository;

import java.util.List;
import roomescape.core.domain.Reservation;

public interface ReservationRepository {
    Long save(final Reservation reservation);

    List<Reservation> findAll();

    List<Reservation> findAllByDateAndThemeId(final String date, final long themeId);

    boolean hasReservationAtTime(final long timeId);

    boolean hasReservationWithTheme(final long themeId);

    boolean hasDuplicateReservation(final String date, final long timeId, final long themeId);

    void deleteById(final long id);
}
