package roomescape.time.repository;

import java.util.List;
import java.util.Optional;

import roomescape.reservation.domain.ReservationTime;

public interface TimeRepository {
  List<ReservationTime> findAll();

  ReservationTime save(String startAt, String endAt);

  Optional<ReservationTime> findById(long id);

  boolean deleteById(long id);
}
