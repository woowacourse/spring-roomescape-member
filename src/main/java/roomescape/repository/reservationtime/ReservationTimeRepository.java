package roomescape.repository.reservationtime;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.dto.other.TimeWithBookState;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(long id);

    List<TimeWithBookState> findAllWithBookState(LocalDate date, long themeId);

    boolean checkExistenceByStartAt(LocalTime startAt);

    long add(ReservationTime reservationTime);

    void deleteById(long id);
}
