package roomescape.repository;

import jakarta.validation.constraints.NotNull;
import roomescape.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationTimeRepository {
    ReservationTime save(ReservationTime time);

    List<ReservationTime> findAll();

    boolean deleteById(Long id);

    ReservationTime findById(@NotNull Long timeId);

    boolean existByTime(LocalTime createTime);

    List<ReservationTime> getAvailableReservationTimeOf(LocalDate date, Long themeId);
}
