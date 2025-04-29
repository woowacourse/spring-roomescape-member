package roomescape.repository;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import roomescape.entity.ReservationTime;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime time);

    List<ReservationTime> findAll();

    int deleteById(Long id);

    ReservationTime findById(@NotNull Long timeId);
}
