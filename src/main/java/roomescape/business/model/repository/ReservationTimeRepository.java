package roomescape.business.model.repository;

import roomescape.business.model.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime time);

    List<ReservationTime> findAll();

    List<ReservationTime> findAvailableReservationTimesByDateAndThemeId(LocalDate date, long themeId);

    Optional<ReservationTime> findById(long timeId);

    boolean existBetween(LocalTime startInclusive, LocalTime endExclusive);

    boolean existById(long timeId);

    boolean existByTime(LocalTime createTime);

    void deleteById(long timeId);
}
