package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.time.AvailableReservationTime;
import roomescape.domain.time.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(LocalTime time);

    List<ReservationTime> findAll();

    void deleteById(Long id);

    Optional<ReservationTime> findById(Long id);

    boolean existByStartAt(LocalTime startAt);

    List<AvailableReservationTime> findAllAvailableReservationTimes(LocalDate date, Long themeId);
}
