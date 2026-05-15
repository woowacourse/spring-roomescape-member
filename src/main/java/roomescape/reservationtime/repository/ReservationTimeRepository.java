package roomescape.reservationtime.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.dto.AvailableTime;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();
    ReservationTime save(LocalTime startAt);
    void delete(long id);
    Optional<ReservationTime> findById(long id);
    List<AvailableTime> findAvailableTimes(Long themeId, LocalDate date);
}
