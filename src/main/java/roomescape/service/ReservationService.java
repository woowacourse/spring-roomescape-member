package roomescape.service;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final Clock clock;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao, Clock clock) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.clock = clock;
    }

    public ReservationResponse create(ReservationRequest request) {
        ReservationTime reservationTime = getTime(request.timeId());
        Theme theme = getTheme(request.themeId());

        Reservation reservation = request.toReservation(reservationTime, theme, clock);
        validateUniqueReservation(theme.getId(), reservation.getDate(), reservationTime.getId());

        Reservation savedReservation = reservationDao.save(reservation);
        return ReservationResponse.from(savedReservation);
    }

    private void validateUniqueReservation(long themeId, LocalDate date, long timeId) {
        boolean exists = reservationDao.existsByThemeAndDateAndTime(themeId, date, timeId);
        if (exists) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_ALREADY_EXISTS);
        }
    }

    private ReservationTime getTime(long timeId) {
        return reservationTimeDao.findById(timeId)
                .orElseThrow(() -> new ReservationTimeException(ReservationTimeErrorCode.RESERVATION_TIME_NOT_FOUND));
    }

    private Theme getTheme(long themeId) {
        return themeDao.findById(themeId)
                .orElseThrow(() -> new ThemeException(ThemeErrorCode.THEME_NOT_FOUND));
    }

    public List<ReservationResponse> getReservations() {
        List<Reservation> reservations = reservationDao.findAll();
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public List<ReservationResponse> getReservationsByName(String name) {
        List<Reservation> reservations = reservationDao.findAllByName(name);
        return reservations.stream()
                .map(ReservationResponse::from)
                .toList();
    }

    public void delete(long reservationId) {
        Reservation reservation = getReservation(reservationId);
        validateCancelable(reservation);
        reservationDao.delete(reservationId);
    }

    private Reservation getReservation(long reservationId) {
        return reservationDao.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ReservationErrorCode.RESERVATION_NOT_FOUND));
    }

    private void validateCancelable(Reservation reservation) {
        LocalDateTime now = LocalDateTime.now(clock);

        if (reservation.isCancelDeadlinePassed(now)) {
            throw new ReservationException(ReservationErrorCode.RESERVATION_CANCEL_DEADLINE_PASSED);
        }
    }
}
