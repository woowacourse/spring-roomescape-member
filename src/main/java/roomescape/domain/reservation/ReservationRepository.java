package roomescape.domain.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.theme.Theme;

public interface ReservationRepository {

    Reservation save(Reservation reservation);

    List<Reservation> findAll();

    int deleteById(Long id);

    int countByTimeId(Long timeId);

    int countByReservationDateId(Long dateId);

    List<Long> findReservedTimes(Long themeId, Long dateId);

    List<Theme> findPopularThemes(int rankLimit, LocalDate startDay, LocalDate today);

    int countByThemeId(Long id);

    boolean existsReservation(Long timeId, Long dateId, Long themeId);

    List<Reservation> findByName(String name);
}
