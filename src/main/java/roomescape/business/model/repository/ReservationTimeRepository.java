package roomescape.business.model.repository;

import roomescape.business.model.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime time);

    Optional<ReservationTime> findById(long timeId);

    List<ReservationTime> findAll();

    boolean existByTime(LocalTime createTime);

    List<ReservationTime> getAvailableReservationTimeOf(LocalDate date, long themeId);

    boolean deleteById(long id);
}
