package roomescape.reservationtime.repository;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime add(ReservationTime time);

    List<ReservationTime> findAll();

    int deleteById(Long id);

    Optional<ReservationTime> findById(Long timeId);

    boolean existsByStartAt(@NotNull LocalTime localTime);
}
