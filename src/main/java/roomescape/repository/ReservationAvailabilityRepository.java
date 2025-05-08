package roomescape.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.business.domain.reservation.ReservationAvailability;
import roomescape.business.domain.theme.Theme;

public interface ReservationAvailabilityRepository {

    List<ReservationAvailability> findAllByDateAndTheme(LocalDate date, Theme theme);
}
