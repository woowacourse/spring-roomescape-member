package roomescape.reservationtime.application.query;

import java.time.LocalDate;
import java.util.List;

public interface AvailableTimeDao {
    List<AvailableReservationTime> findByThemeAndDate(Long themeId, LocalDate date);
}
