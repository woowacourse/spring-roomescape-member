package roomescape.persistence;

import java.time.LocalDate;
import java.util.List;
import roomescape.business.ReservationTheme;

public interface ReservationThemeRepository {

    List<ReservationTheme> findAll();

    Long add(ReservationTheme reservationTheme);

    boolean existByName(String name);

    void deleteById(Long id);

    ReservationTheme findById(Long aLong);

    List<ReservationTheme> findByStartDateAndEndDateOrderByReservedDesc(LocalDate start, LocalDate end, int limit);
}
