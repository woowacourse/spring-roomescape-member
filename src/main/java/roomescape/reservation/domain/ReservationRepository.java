package roomescape.reservation.domain;

import roomescape.theme.domain.ThemeId;
import roomescape.time.domain.ReservationTimeId;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    boolean existsByParams(ReservationId id);

    boolean existsByParams(ReservationTimeId timeId);

    boolean existsByParams(ReservationDate date, ReservationTimeId timeId, ThemeId themeId);

    Optional<Reservation> findById(ReservationId id);

    List<ReservationTimeId> findTimeIdByParams(ReservationDate date, ThemeId themeId);

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(ReservationId id);
}
