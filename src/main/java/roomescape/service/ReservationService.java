package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.dto.request.ReservationRequest;
import roomescape.dto.response.ReservationResponse;
import roomescape.exception.code.ReservationErrorCode;
import roomescape.exception.code.ReservationTimeErrorCode;
import roomescape.exception.code.ThemeErrorCode;
import roomescape.exception.domain.ReservationException;
import roomescape.exception.domain.ReservationTimeException;
import roomescape.exception.domain.ThemeException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public ReservationResponse addReservation(ReservationRequest request) {
        ReservationTime reservationTime = getTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        Reservation reservation = request.toReservation(reservationTime, theme);
        Reservation savedReservation = reservationDao.insert(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private ReservationTime getTime(long timeId) {
        return reservationTimeDao.selectById(timeId)
                .orElseThrow(() -> new ReservationTimeException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    private Theme getTheme(long themeId) {
        return themeDao.selectById(themeId)
                .orElseThrow(() -> new ThemeException(ThemeErrorCode.THEME_NOT_FOUND));
    }

    public List<ReservationResponse> getAllReservations() {
        List<Reservation> reservations = reservationDao.selectAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void delete(Long reservationId) {
        validateReservationExists(reservationId);
        reservationDao.delete(reservationId);
    }

    private void validateReservationExists(Long reservationId) {
        boolean exists = reservationDao.existsById(reservationId);
        if (!exists) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND);
        }
    }
}
