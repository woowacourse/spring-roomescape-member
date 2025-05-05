package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeWithBookState;

public interface ReservationTimeRepository {

    List<ReservationTime> findAll();

    Optional<ReservationTime> findById(long id);

    boolean findByStartAt(LocalTime startAt);

    List<ReservationTimeWithBookState> findAllWithBookState(LocalDate date, long themeId);

    long add(ReservationTime reservationTime);

    void deleteById(long id);
}
