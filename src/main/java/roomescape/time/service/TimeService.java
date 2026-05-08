package roomescape.time.service;

import java.util.List;

import roomescape.time.domain.ReservationTime;

public interface TimeService {
  ReservationTime create(String startAt, String endAt);

  List<ReservationTime> findAll();

  ReservationTime findById(Long id);

  ReservationTime findByStartAt(String startAt);

  void deleteById(Long id);
}
