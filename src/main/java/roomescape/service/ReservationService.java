package roomescape.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.controller.dto.ReservationRequest;
import roomescape.controller.dto.ReservationResponse;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;
import roomescape.service.reservation.Reservation;
import roomescape.service.reservation.ReservationTime;
import roomescape.service.reservation.Theme;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(final ReservationDao reservationDao, final ReservationTimeDao reservationTimeDao,
                              final ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse createReservation(final ReservationRequest reservationRequest) {
        final ReservationTime reservationTime = findReservationTime(reservationRequest.timeId());
        final Theme theme = findTheme(reservationRequest.themeId());
        final Reservation reservation = reservationRequest.convertToReservation(reservationTime, theme);
        if (reservation.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("지나간 날짜와 시간은 예약 불가합니다.");
        }
        if (reservationDao.isExistsByDateAndTimeId(reservation.getDate(), reservation.getTimeId())) {
            throw new IllegalArgumentException("해당 시간에 이미 예약이 존재합니다.");
        }
        final Reservation savedReservation = reservationDao.save(reservation);
        return new ReservationResponse(savedReservation);
    }

    public List<ReservationResponse> getReservations() {
        return reservationDao.findAll().stream()
                .map(ReservationResponse::new)
                .toList();
    }

    public void cancelReservationById(final long id) {
        reservationDao.deleteById(id);
    }

    private ReservationTime findReservationTime(final long timeId) {
        if (reservationTimeDao.isNotExistsById(timeId)) {
            throw new IllegalArgumentException("예약 시간이 존재하지 않습니다.");
        }
        return reservationTimeDao.findById(timeId);
    }

    private Theme findTheme(final long themeId) {
        if (themeDao.isNotExistsById(themeId)) {
            throw new IllegalArgumentException("테마가 존재하지 않습니다.");
        }
        return themeDao.findById(themeId);
    }
}
