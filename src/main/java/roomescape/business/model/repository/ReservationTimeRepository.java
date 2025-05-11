package roomescape.business.model.repository;

import roomescape.business.model.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    void save(ReservationTime time);

    List<ReservationTime> findAll();

    List<ReservationTime> findAvailableByDateAndThemeId(LocalDate date, String themeId);

    List<ReservationTime> findNotAvailableByDateAndThemeId(LocalDate date, String themeId);

    Optional<ReservationTime> findById(String timeId);

    boolean existBetween(LocalTime startInclusive, LocalTime endExclusive);

    boolean existById(String timeId);

    boolean existByTime(LocalTime createTime);

    void deleteById(String timeId);
}
