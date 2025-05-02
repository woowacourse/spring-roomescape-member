package roomescape.repository.reservationtime;

import jakarta.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.entity.ReservationTimeEntity;

public interface ReservationTimeRepository {
    ReservationTimeEntity add(ReservationTime time);

    List<ReservationTimeEntity> findAll();

    int deleteById(Long id);

    Optional<ReservationTimeEntity> findById(Long timeId);

    boolean existsByStartAt(@NotNull LocalTime localTime);
}
