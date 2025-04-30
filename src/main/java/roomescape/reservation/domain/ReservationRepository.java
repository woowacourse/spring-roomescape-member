package roomescape.reservation.domain;

import roomescape.theme.domain.ThemeId;
import roomescape.time.domain.ReservationTimeId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    boolean existsByParams(ReservationId id);

    boolean existsByParams(ReservationTimeId timeId);

    boolean existsByParams(LocalDate date, ReservationTimeId timeId, ThemeId themeId);

    Optional<Reservation> findById(ReservationId id);

    List<ReservationTimeId> findTimeIdByParams(ReservationDate date, ThemeId themeId);

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    void deleteById(ReservationId id);
}
