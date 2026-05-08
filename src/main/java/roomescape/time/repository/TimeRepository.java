package roomescape.time.repository;

import java.util.List;
import java.util.Optional;

import roomescape.time.domain.ReservationTime;

public interface TimeRepository {
  List<ReservationTime> findAll();

  ReservationTime save(String startAt, String endAt);

  Optional<ReservationTime> findById(Long id);

  Optional<ReservationTime> findByStartAt(String startAt);

  boolean existsById(Long id);

  boolean deleteById(Long id);
}
