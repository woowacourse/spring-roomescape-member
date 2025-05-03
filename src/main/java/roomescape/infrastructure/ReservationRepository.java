package roomescape.infrastructure;

import java.util.List;
import java.util.Optional;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationDateTime;
import roomescape.domain.ReserverName;
import roomescape.domain.Theme;

public interface ReservationRepository {

    List<Reservation> findAll();

    Reservation save(ReserverName reserverName, ReservationDateTime reservationDateTime, Theme theme);

    void deleteById(Long id);

    Optional<Reservation> findById(Long id);

    boolean existSameDateTime(ReservationDate reservationDate, Long timeId);

    boolean existReservationByTimeId(Long timeId);

    boolean existReservationByThemeId(Long themeId);
}
