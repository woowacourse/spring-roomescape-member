package roomescape.reservationtime.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.repositoy.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.domain.ReservationTimeAvailabilities;
import roomescape.reservationtime.dto.SaveReservationTimeRequest;
import roomescape.reservationtime.repository.ReservationTimeRepository;

@Service
public class ReservationTimeService {

  private final ReservationTimeRepository reservationTimeRepository;
  private final ReservationRepository reservationRepository;

  public ReservationTimeService(final ReservationTimeRepository reservationTimeRepository,
      final ReservationRepository reservationRepository) {
    this.reservationTimeRepository = reservationTimeRepository;
    this.reservationRepository = reservationRepository;
  }

  public List<ReservationTime> getReservationTimes() {
    return reservationTimeRepository.findAll();
  }

  public ReservationTime saveReservationTime(final SaveReservationTimeRequest request) {
    validateReservationTimeDuplication(request);

    return reservationTimeRepository.save(request.toReservationTime());
  }

  private void validateReservationTimeDuplication(final SaveReservationTimeRequest request) {
    if (reservationTimeRepository.existByStartAt(request.startAt())) {
      throw new IllegalArgumentException("이미 존재하는 예약시간이 있습니다.");
    }
  }

  public void deleteReservationTime(final Long reservationTimeId) {
    checkReservationTimeExist(reservationTimeId);
    validateReservationTimeExist(reservationTimeId);
    reservationTimeRepository.deleteById(reservationTimeId);
  }

  private void checkReservationTimeExist(final Long reservationTimeId) {
    if (!reservationTimeRepository.existById(reservationTimeId)) {
      throw new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다.");
    }
  }

  private void validateReservationTimeExist(final Long reservationTimeId) {
    if (reservationRepository.existByTimeId(reservationTimeId)) {
      throw new IllegalArgumentException("예약에 포함된 시간 정보는 삭제할 수 없습니다.");
    }
  }

  public ReservationTimeAvailabilities getAvailableReservationTimes(final LocalDate date,
      final Long themeId) {
    final List<ReservationTime> reservationTimes = reservationTimeRepository.findAll();
    final List<Reservation> reservations = reservationRepository.findAllByDateAndThemeId(date,
        themeId);

    return ReservationTimeAvailabilities.of(reservationTimes, reservations);
  }
}
