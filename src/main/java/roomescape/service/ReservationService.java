package roomescape.service;

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
import roomescape.dto.ReservationRequest;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.NotCorrectDateTimeException;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public Reservation addReservationAfterNow(ReservationRequest request) {
        LocalDate date = request.date();
        ReservationTime time = reservationTimeDao.findTimeById(request.timeId());
        validateDateTimeAfterNow(date, time);

        return addReservation(request);
    }

    private void validateDateTimeAfterNow(LocalDate date, ReservationTime time) {
        LocalDateTime now = LocalDateTime.now();
        if (date.isBefore(now.toLocalDate()) ||
            (date.isEqual(now.toLocalDate()) && time.isBefore(now.toLocalTime()))) {
            throw new NotCorrectDateTimeException("지나간 날짜와 시간에 대한 예약 생성은 불가능하다.");
        }
    }

    public Reservation addReservation(ReservationRequest reservationRequest) {
        validateDuplicateReservation(reservationRequest);
        ReservationTime time = reservationTimeDao.findTimeById(reservationRequest.timeId());
        Theme theme = themeDao.findThemeById(reservationRequest.themeId());
        return reservationDao.addReservation(
            new Reservation(null, reservationRequest.name(), reservationRequest.date(), time,
                theme));
    }

    private void validateDuplicateReservation(ReservationRequest reservationRequest) {
        if (reservationDao.existReservationByDateTimeAndTheme(reservationRequest.date(),
            reservationRequest.timeId(), reservationRequest.themeId())) {
            throw new DuplicateReservationException();
        }
    }

    public List<Reservation> findAllReservations() {
        return reservationDao.findAllReservations();
    }

    public void removeReservation(Long id) {
        reservationDao.removeReservationById(id);
    }
}
