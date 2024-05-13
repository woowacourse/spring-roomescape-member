package roomescape.reservation.service;

import java.time.LocalDate;
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

  public List<Reservation> getReservations(LocalDate dateFrom, LocalDate dateTo, Long themeId,
      Long memberId) {
    return reservationRepository.findAll(dateFrom, dateTo, themeId, memberId);
  }

  public Reservation saveReservation(Member member, final SaveReservationRequest request) {
    final ReservationTime reservationTime = checkReservationTimeExist(
        request.timeId());
    final Theme theme = checkThemeExist(request.themeId());

    validateReservationDuplication(request.date(), request.timeId(), request.themeId());

    return reservationRepository.save(
        request.toReservation(member, reservationTime, theme));
  }

  private ReservationTime checkReservationTimeExist(Long reservationTimeId) {
    return reservationTimeRepository.findById(reservationTimeId)
        .orElseThrow(() -> new NoSuchElementException("해당 id의 예약 시간이 존재하지 않습니다."));
  }

  private Theme checkThemeExist(Long themeId) {
    return themeRepository.findById(themeId)
        .orElseThrow(() -> new NoSuchElementException("해당 id의 테마가 존재하지 않습니다."));
  }

  private void validateReservationDuplication(LocalDate date, Long timeId, Long themeId) {
    if (reservationRepository.existByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
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
    final ReservationTime reservationTime = checkReservationTimeExist(
        request.timeId());
    final Theme theme = checkThemeExist(request.themeId());

    validateReservationDuplication(request.date(), request.timeId(), request.themeId());

    return reservationRepository.save(
        request.toReservation(member, reservationTime, theme));
  }
}
