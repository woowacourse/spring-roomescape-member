package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;

public interface ReservationRepository {
    Long save(final Reservation reservation);

    List<Reservation> findAllByDateAndThemeId(final String date, final long themeId);

    List<Reservation> findAllWithConditions(
            final Long memberId,
            final Long themeId,
            final LocalDate dateFrom,
            final LocalDate dateTo
    );

    boolean hasReservationAtTime(final long timeId);

    boolean hasReservationWithTheme(final long themeId);

    boolean hasDuplicateReservation(final String date, final long timeId, final long themeId);

    void deleteById(final long id);
}
