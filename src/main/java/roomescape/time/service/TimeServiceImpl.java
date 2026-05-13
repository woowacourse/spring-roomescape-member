package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.exception.TimeNotFoundException;
import roomescape.time.repository.TimeRepository;

@Service
public class TimeServiceImpl implements TimeService {
  private final TimeRepository timeRepository;
  private final ReservationRepository reservationRepository;

  public TimeServiceImpl(TimeRepository timeRepository, ReservationRepository reservationRepository) {
    this.timeRepository = timeRepository;
    this.reservationRepository = reservationRepository;
  }

  @Override
  public ReservationTime create(String startAt, String endAt) {
    ReservationTime time = new ReservationTime(startAt, endAt);
    return timeRepository.save(time.getStartAt(), time.getEndAt());
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
    LocalTime startTime = ReservationTime.parse(startAt);
    return timeRepository.findByStartAt(startTime)
        .orElseThrow(() -> new IllegalArgumentException("예약 시간이 존재하지 않습니다. startAt=" + startAt));
  }

  @Override
  public void deleteById(Long id) {
    if (!timeRepository.existsById(id)) {
      throw new TimeNotFoundException(id);
    }
    if (reservationRepository.existsByTimeId(id)) {
      throw new RoomescapeException(ErrorCode.RESERVED_TIME_DELETE_NOT_ALLOWED);
    }
    timeRepository.deleteById(id);
  }
}
