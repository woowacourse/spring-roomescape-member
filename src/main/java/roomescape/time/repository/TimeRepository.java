package roomescape.time.repository;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import roomescape.time.domain.ReservationTime;

public interface TimeRepository {
  List<ReservationTime> findAll();

  ReservationTime save(LocalTime startAt, LocalTime endAt);

  Optional<ReservationTime> findById(Long id);

  Optional<ReservationTime> findByStartAt(LocalTime startAt);

  boolean existsById(Long id);

  boolean deleteById(Long id);
}
