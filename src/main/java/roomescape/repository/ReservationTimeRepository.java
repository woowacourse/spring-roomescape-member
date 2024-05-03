package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> findByReserved(LocalDate date, Long themeId);

    ReservationTime save(ReservationTime reservationTime);

    boolean existByStartAt(LocalTime startAt);

    void deleteById(Long id);
}
