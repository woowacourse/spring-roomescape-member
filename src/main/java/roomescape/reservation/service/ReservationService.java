package roomescape.reservation.service;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.reservation.dao.ReservationDao;
import roomescape.theme.dao.ThemeDao;
import roomescape.time.dao.TimeDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationCreateRequest;
import roomescape.reservation.dto.ReservationResponse;
import roomescape.time.domain.ReservationTime;
import roomescape.theme.domain.Theme;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public List<ReservationResponse> findReservations() {
        return reservationDao.findReservations()
                .stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public ReservationResponse createReservation(ReservationCreateRequest request) {
        ReservationTime time = findTimeByTimeId(request.timeId());
        Theme theme = findThemeByThemeId(request.themeId());
        Reservation reservation = request.createReservation(time, theme);

        validateIsAvailable(reservation);
        Reservation createdReservation = reservationDao.createReservation(reservation);
        return ReservationResponse.from(createdReservation);
    }

    private ReservationTime findTimeByTimeId(Long timeId) {
        return timeDao.findTimeById(timeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 예약 시간이 존재하지 않습니다."));
    }

    private Theme findThemeByThemeId(Long themeId) {
        return themeDao.findThemeById(themeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 테마가 존재하지 않습니다."));
    }

    private void validateIsAvailable(Reservation reservation) {
        if (reservation.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("예약은 현재 시간 이후여야 합니다.");
        }
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteReservation(id);
    }
}
