package roomescape.time.service;

import java.util.List;

import roomescape.reservation.domain.ReservationTime;

public interface TimeService {
  ReservationTime create(String startAt, String endAt);

  List<ReservationTime> findAll();

  ReservationTime findById(Long id);

  void deleteById(Long id);
}
