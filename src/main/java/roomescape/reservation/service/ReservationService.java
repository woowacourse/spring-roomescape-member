package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationDAO;
import roomescape.reservation.dao.ReservationTimeDAO;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationTime;
import roomescape.reservation.dto.request.ReservationRequest;
import roomescape.reservation.dto.response.ReservationCreateResponse;
import roomescape.reservation.dto.response.ReservationResponse;
import roomescape.reservation.dto.response.ThemeSimpleResponse;
import roomescape.reservation.dto.response.TimeResponse;

@Service
public class ReservationService {

  private final ReservationDAO reservationDAO;
  private final ReservationTimeDAO reservationTimeDAO;

  public ReservationService(ReservationDAO reservationDAO, ReservationTimeDAO reservationTimeDAO) {
    this.reservationDAO = reservationDAO;
    this.reservationTimeDAO = reservationTimeDAO;
  }

  public ReservationCreateResponse create(ReservationRequest request) {
    ReservationTime reservationTime = reservationTimeDAO.findById(request.timeId());
    isAfterDate(request);
    isAfterTimeAtSameDate(request, reservationTime);

    isReservationExists(request);

    Reservation reservation = reservationDAO.insert(request.name(), LocalDate.parse(request.date()),
        request.timeId(), request.themeId());

    return ReservationCreateResponse.from(reservation);
  }

  public List<ReservationResponse> findAll() {
    return reservationDAO.findAll().stream()
        .map(reservation -> ReservationResponse.of(
            reservation.getId(),
            reservation.getName(),
            reservation.getDate(),
            TimeResponse.from(reservation.getTime()),
            ThemeSimpleResponse.from(reservation.getTheme())
        )).toList();
  }

  public List<ReservationResponse> findByName(String name) {
    return reservationDAO.findByName(name).stream()
        .map(reservation -> ReservationResponse.of(
            reservation.getId(),
            reservation.getName(),
            reservation.getDate(),
            TimeResponse.from(reservation.getTime()),
            ThemeSimpleResponse.from(reservation.getTheme())
        )).toList();
  }

  public ReservationResponse findById(Long id) {
    Reservation reservation = reservationDAO.findById(id);
    return ReservationResponse.of(reservation.getId(), reservation.getName(), reservation.getDate(),
        TimeResponse.from(reservation.getTime()), ThemeSimpleResponse.from(reservation.getTheme()));
  }

  public void delete(Long id) {
    reservationDAO.delete(id);
  }

  public void deleteByNameAndReservationId(String name, Long reservationId) {
    boolean isExistReservation = reservationDAO.existsByNameAndReservationId(name, reservationId);
    if (!isExistReservation) {
      throw new IllegalStateException("해당 예약이 이미 존재하지 않습니다.");
    }

    reservationDAO.deleteByNameAndReservationId(name, reservationId);
  }

  public boolean existsByTimeId(Long timeId) {
    return reservationDAO.existsByTimeId(timeId);
  }

  private static void isAfterDate(ReservationRequest request) {
    if (LocalDate.parse(request.date()).isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("지나간 날짜에 대한 예약 생성은 불가능합니다.");
    }
  }

  private static void isAfterTimeAtSameDate(ReservationRequest request,
      ReservationTime reservationTime) {
    if (LocalDate.parse(request.date()).isEqual(LocalDate.now())
        && reservationTime.getStartAt().isBefore(LocalTime.now())) {
      throw new IllegalArgumentException("당일 지나간 시간에 대한 예약 생성은 불가능합니다.");
    }
  }

  private void isReservationExists(ReservationRequest request) {
    boolean reservationExist = reservationDAO.existsByTimeIdAndThemeId(request.timeId(),
        request.themeId());
    if (reservationExist) {
      throw new IllegalStateException("해당 시간대는 이미 예약이 완료되었습니다.");
    }
  }
}
