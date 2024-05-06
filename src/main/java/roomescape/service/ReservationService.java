package roomescape.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationThemeDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTheme;
import roomescape.domain.ReservationTime;
import roomescape.dto.reservation.ReservationRequest;
import roomescape.dto.reservation.ReservationResponse;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ReservationThemeDao reservationThemeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                              ReservationThemeDao reservationThemeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.reservationThemeDao = reservationThemeDao;
    }

    public List<ReservationResponse> getAllReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    @Transactional
    public ReservationResponse insertReservation(ReservationRequest reservationRequest) {
        ReservationTime reservationTime = reservationTimeDao.findById(reservationRequest.timeId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 입력입니다."));
        LocalDate date = reservationRequest.date();
        validatePast(date, reservationTime.getStartAt());
        validateDuplicated(date, reservationRequest.timeId(), reservationRequest.themeId());
        Long id = reservationDao.insert(
                reservationRequest.name(), reservationRequest.date().toString(), reservationRequest.timeId(),
                reservationRequest.themeId());
        ReservationTheme reservationTheme = reservationThemeDao.findById(reservationRequest.themeId())
                .orElseThrow(() -> new IllegalArgumentException("올바르지 않은 입력입니다."));
        Reservation inserted = new Reservation(id, reservationRequest.name(), reservationRequest.date().toString(),
                reservationTime, reservationTheme);
        return new ReservationResponse(inserted);
    }

    private void validatePast(LocalDate localDate, LocalTime localTime) {
        LocalDateTime inputDateTime = LocalDateTime.of(localDate, localTime);
        if (LocalDateTime.now().isAfter(inputDateTime)) {
            throw new IllegalArgumentException("지나간 날짜와 시간에 대한 예약 생성은 불가능합니다.");
        }
    }

    private void validateDuplicated(LocalDate date, Long timeId, Long themeId) {
        if (reservationDao.count(date.toString(), timeId, themeId) != 0) {
            throw new IllegalArgumentException("이미 해당 시간에 예약이 존재합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteById(id);
    }
}
