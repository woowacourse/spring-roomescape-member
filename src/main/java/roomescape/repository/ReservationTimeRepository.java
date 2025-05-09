package roomescape.repository;

import roomescape.domain.ReservationTime;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// todo: repository naming 고민
public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    void deleteById(Long id);

    List<ReservationTime> findByReservationDateAndThemeId(LocalDate date, Long themeId);
}
