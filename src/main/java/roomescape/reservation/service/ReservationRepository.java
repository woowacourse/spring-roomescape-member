package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDate;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.domain.ReserverName;
import roomescape.theme.domain.Theme;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(Reservation reservation);

    Reservation save(ReserverName reserverName, ReservationDateTime reservationDateTime, Theme theme);

    Optional<Reservation> findById(Long id);

    void deleteById(Long id);

    boolean existSameDateTime(ReservationDate reservationDate, Long timeId);

    boolean existReservationByTimeId(Long timeId);

    boolean existReservationByThemeId(Long themeId);

    boolean existsByTimeIdAndDateAndThemeId(Long id, LocalDate date, Long themeId);
}
