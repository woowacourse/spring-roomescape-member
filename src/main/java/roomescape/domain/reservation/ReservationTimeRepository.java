package roomescape.domain.reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationTimeRepository {

    ReservationTime save(ReservationTime reservationTime);

    List<ReservationTime> findAll();

    void deleteById(long id);

    Optional<ReservationTime> findById(long id);

    ReservationTime getById(Long id);

    boolean existsByTime(String startAt);

    List<ReservationTime> findBookedTimesByDateAndTheme(String date, long themeId);
}
