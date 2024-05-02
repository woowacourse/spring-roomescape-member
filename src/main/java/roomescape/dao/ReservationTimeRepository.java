package roomescape.dao;

import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(long id);

    Optional<ReservationTime> findById(long id);

    boolean existsByTime(String startAt);

    List<ReservationTime> findByThemeAndDate(long themeId, String date);
}
