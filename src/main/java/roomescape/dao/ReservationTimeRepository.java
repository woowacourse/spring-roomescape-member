package roomescape.dao;

import roomescape.domain.ReservationTime;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(long id);

    Optional<ReservationTime> findById(long id);

    boolean existsByTime(String startAt);

    List<ReservationTime> findByDateAndTheme(String date, long themeId);
}
