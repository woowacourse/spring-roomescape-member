package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.business.domain.ReservationAvailability;
import roomescape.business.domain.Theme;

public interface ReservationAvailabilityRepository {

    List<ReservationAvailability> findAllByDateAndTheme(LocalDate date, Theme theme);
}
