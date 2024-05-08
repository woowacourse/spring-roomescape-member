package roomescape.repository.reservationtime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    void delete(Long id);

    Optional<ReservationTime> findById(Long id);

    List<ReservationTime> findAll();

    Map<ReservationTime, Boolean> findAllWithAlreadyBooked(LocalDate date, Long themeId);

    boolean isDuplicatedTime(LocalTime localTime);
}
