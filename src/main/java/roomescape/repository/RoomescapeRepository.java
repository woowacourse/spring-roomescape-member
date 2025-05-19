package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;

public interface RoomescapeRepository {

    Optional<Reservation> findById(final Long id);

    List<Reservation> findByDate(final LocalDate date);

    List<Reservation> findAll(final Long memberId, final Long themeId, final LocalDate dateFrom,
                              final LocalDate dateTo);

    Reservation save(final Reservation reservation);

    boolean deleteById(final Long id);

    boolean existsByThemeId(final Long themeId);

    boolean existsByTimeId(final Long timeId);

    boolean existsByDateAndTime(final LocalDate date, final ReservationTime time);
}
