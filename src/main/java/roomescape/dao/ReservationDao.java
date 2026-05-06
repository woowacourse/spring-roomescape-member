package roomescape.dao;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

public interface ReservationDao {
    Reservation create(Reservation reservation, ReservationTime reservationTime, Theme theme);

    List<Reservation> readAll();

    void delete(Long id);

    boolean existByDateAndTimeIdAndThemeId(LocalDate date, Long timeId, Long themeId);
}
