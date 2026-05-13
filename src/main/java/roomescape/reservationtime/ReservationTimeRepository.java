package roomescape.reservationtime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {
    List<ReservationTime> findAll();
    ReservationTime save(LocalTime startAt);
    void delete(long id);
    Optional<ReservationTime> findById(long id);
    List<AvailableTime> findAvailableTimes(Long themeId, LocalDate date);
}
