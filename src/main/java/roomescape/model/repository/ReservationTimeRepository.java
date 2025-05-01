package roomescape.model.repository;

import roomescape.model.entity.ReservationTime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime time);

    ReservationTime findById(long timeId);

    List<ReservationTime> findAll();

    boolean existByTime(LocalTime createTime);

    List<ReservationTime> getAvailableReservationTimeOf(LocalDate date, long themeId);

    boolean deleteById(long id);
}
