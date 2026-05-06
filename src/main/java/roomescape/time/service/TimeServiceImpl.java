package roomescape.time.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.reservation.domain.ReservationTime;
import roomescape.time.exception.TimeNotFoundException;
import roomescape.time.repository.TimeRepository;

@Service
public class TimeServiceImpl implements TimeService {
  private final TimeRepository timeRepository;

  public TimeServiceImpl(TimeRepository timeRepository) {
    this.timeRepository = timeRepository;
  }

  @Override
  public ReservationTime create(String startAt, String endAt) {
    return timeRepository.save(startAt, endAt);
  }

  @Override
  public List<ReservationTime> findAll() {
    return timeRepository.findAll();
  }

  @Override
  public ReservationTime findById(Long id) {
    return timeRepository.findById(id)
        .orElseThrow(() -> new TimeNotFoundException(id));
  }

  @Override
  public ReservationTime findByStartAt(String startAt) {
    if (startAt == null || startAt.isBlank()) {
      throw new IllegalArgumentException("예약 시간은 필수입니다.");
    }
    return timeRepository.findByStartAt(startAt)
        .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다. startAt=" + startAt));
  }

  @Override
  public void deleteById(Long id) {
    boolean deleted = timeRepository.deleteById(id);
    if (!deleted) {
      throw new TimeNotFoundException(id);
    }
  }

}
