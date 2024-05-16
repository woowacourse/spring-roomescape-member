package roomescape.reservationtime.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import roomescape.reservationtime.domain.ReservationTime;

public interface ReservationTimeRepository {

  List<ReservationTime> findAll();

  Optional<ReservationTime> findById(Long reservationTimeId);

  ReservationTime save(ReservationTime reservationTime);

  void deleteById(Long reservationTimeId);

  boolean existById(Long reservationTimeId);

  boolean existByStartAt(LocalTime startAt);
}
