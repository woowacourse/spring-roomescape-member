package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.ReservationAvailability;
import roomescape.domain.Theme;

public interface ReservationAvailabilityDao {

    List<ReservationAvailability> findAllByDateAndTheme(LocalDate date, Theme theme);
}
