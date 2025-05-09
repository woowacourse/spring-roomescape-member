package roomescape.persistence;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import roomescape.business.domain.reservation.ReservationTheme;

public interface ReservationThemeRepository {

    List<ReservationTheme> findAll();

    ReservationTheme add(ReservationTheme reservationTheme);

    boolean existByName(String name);

    void deleteById(Long id);

    Optional<ReservationTheme> findById(Long aLong);

    List<ReservationTheme> findByStartDateAndEndDateOrderByReservedDesc(LocalDate start, LocalDate end, int limit);
}
