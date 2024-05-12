package roomescape.reservation.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import roomescape.admin.dto.SaveAdminReservationRequest;
import roomescape.auth.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.SaveReservationRequest;
import roomescape.reservation.repositoy.ReservationRepository;
import roomescape.reservationtime.domain.ReservationTime;
import roomescape.reservationtime.repository.ReservationTimeRepository;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeRepository;

@Service
public class ReservationService {

  private final ReservationRepository reservationRepository;
  private final ReservationTimeRepository reservationTimeRepository;
  private final ThemeRepository themeRepository;

  public ReservationService(
      final ReservationRepository reservationRepository,
      final ReservationTimeRepository reservationTimeRepository,
      final ThemeRepository themeRepository
  ) {
    this.reservationRepository = reservationRepository;
    this.reservationTimeRepository = reservationTimeRepository;
    this.themeRepository = themeRepository;
  }

  public List<Reservation> getReservations() {
    return reservationRepository.findAll();
  }

  public Reservation saveReservation(Member member, final SaveReservationRequest request) {
    final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
        .orElseThrow(() -> new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다."));
    final Theme theme = themeRepository.findById(request.themeId())
        .orElseThrow(() -> new NoSuchElementException("해당 id의 테마가 존재하지 않습니다."));

    validateReservationDuplication(request);

    return reservationRepository.save(
        request.toReservation(member, reservationTime, theme));
  }

  private void validateReservationDuplication(final SaveReservationRequest request) {
    if (reservationRepository.existByDateAndTimeIdAndThemeId(request.date(), request.timeId(),
        request.themeId())) {
      throw new IllegalArgumentException("이미 해당 날짜/시간의 테마 예약이 있습니다.");
    }
  }

  public void deleteReservation(final Long reservationId) {
    checkReservationExist(reservationId);
    reservationRepository.deleteById(reservationId);
  }

  private void checkReservationExist(final Long reservationId) {
    reservationRepository.findById(reservationId)
        .orElseThrow(() -> new NoSuchElementException("해당 id의 예약이 존재하지 않습니다."));
  }

  public Reservation saveAdminReservation(Member member, SaveAdminReservationRequest request) {
    final ReservationTime reservationTime = reservationTimeRepository.findById(request.timeId())
        .orElseThrow(() -> new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다."));
    final Theme theme = themeRepository.findById(request.themeId())
        .orElseThrow(() -> new NoSuchElementException("해당 id의 테마가 존재하지 않습니다."));

    if (reservationRepository.existByDateAndTimeIdAndThemeId(request.date(), request.timeId(),
        request.themeId())) {
      throw new IllegalArgumentException("이미 해당 날짜/시간의 테마 예약이 있습니다.");
    }
    return reservationRepository.save(
        request.toReservation(member, reservationTime, theme));
  }
}
