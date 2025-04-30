package roomescape.repository.reservation;

import java.time.LocalDate;
import java.util.List;
import roomescape.domain.Reservation;
import roomescape.entity.ReservationEntity;

public interface ReservationRepository {
    ReservationEntity add(Reservation reservation);

    int deleteById(Long id);

    List<ReservationEntity> findAll();

    boolean existsByDateAndTime(LocalDate date, Long timeId);

    boolean existsByTimeId(Long id);

    List<Long> findTimeIdsByDateAndTheme(LocalDate date, Long themeId);

    List<Long> findTopThemesByReservationCountBetween(LocalDate startDate, LocalDate endDate);
}
