package roomescape.time.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.error.ErrorCode;
import roomescape.error.RoomescapeException;
import roomescape.reservation.repository.ReservationRepository;
import roomescape.time.domain.ReservationTime;
import roomescape.time.exception.TimeException;
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
    ReservationTime time = new ReservationTime(ReservationTime.parse(startAt), ReservationTime.parse(endAt));
    return timeRepository.save(time.getStartAt(), time.getEndAt());
  }

  @Override
  public List<ReservationTime> findAll() {
    return timeRepository.findAll();
  }

  @Override
  public ReservationTime findById(Long id) {
    return timeRepository.findById(id)
        .orElseThrow(() -> new TimeException(ErrorCode.TIME_NOT_FOUND,
            "Reservation time not found. timeId=%d".formatted(id)));
  }

  @Override
  public ReservationTime findByStartAt(String startAt) {
    if (startAt == null || startAt.isBlank()) {
      throw new RoomescapeException(ErrorCode.INVALID_REQUEST,
          "Reservation time startAt is required.");
    }
    LocalTime startTime = ReservationTime.parse(startAt);
    return timeRepository.findByStartAt(startTime)
        .orElseThrow(() -> new TimeException(ErrorCode.TIME_NOT_FOUND,
            "Reservation time not found. startAt=%s".formatted(startTime)));
  }

  @Override
  public void deleteById(Long id) {
    if (!timeRepository.existsById(id)) {
      throw new TimeException(ErrorCode.TIME_NOT_FOUND,
          "Reservation time not found. timeId=%d".formatted(id));
    }
    if (reservationRepository.existsByTimeId(id)) {
      throw new TimeException(ErrorCode.RESERVED_TIME_DELETE_NOT_ALLOWED,
          "Reservation time is in use. timeId=%d".formatted(id));
    }
    timeRepository.deleteById(id);
  }
}
