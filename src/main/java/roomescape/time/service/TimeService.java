package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;

import roomescape.reservation.domain.ReservationTime;

public interface TimeService {
  ReservationTime create(LocalTime startAt, LocalTime endAt);

  List<ReservationTime> findAll();

  ReservationTime findById(Long id);

  void deleteById(Long id);
}
