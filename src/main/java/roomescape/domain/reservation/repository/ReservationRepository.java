package roomescape.domain.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.reservation.entity.Reservation;
import roomescape.domain.theme.entity.Theme;
import roomescape.domain.time.entity.Time;

public interface ReservationRepository {

    List<Reservation> findAllReservations();

    List<Long> findTimeIdsByDateAndThemeId(LocalDate localDate, Long themeId);

    Reservation save(Reservation reservation);

    void deleteReservationById(Long id);

    boolean existsReservationByDateAndTimeAndTheme(LocalDate date, Time time, Theme theme);
}
