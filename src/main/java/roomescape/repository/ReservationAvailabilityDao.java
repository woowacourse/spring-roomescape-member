package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.service.reservation.ReservationAvailability;
import roomescape.service.reservation.Theme;

public interface ReservationAvailabilityDao {

    List<ReservationAvailability> findAllByDateAndTheme(LocalDate date, Theme theme);
}
