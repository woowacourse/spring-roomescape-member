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
import roomescape.dto.request.ReservationRequest;
import roomescape.exception.custom.DuplicatedException;
import roomescape.exception.custom.InvalidInputException;

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

    public List<Reservation> findAllReservations() {
        return reservationDao.findAllReservations();
    }

    public Reservation addReservation(ReservationRequest request) {
        validateDuplicateReservation(request);

        ReservationTime time = reservationTimeDao.findTimeById(request.timeId());
        Theme theme = themeDao.findThemeById(request.themeId());

        return reservationDao.addReservation(
            new Reservation(request.name(), request.date(), time, theme));
    }

    private void validateDuplicateReservation(ReservationRequest request) {
        if (reservationDao.existReservationByDateTimeAndTheme(
            request.date(), request.timeId(), request.themeId())) {
            throw new DuplicatedException("reservation");
        }
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
            throw new InvalidInputException("과거 예약은 불가능");
        }
    }

    public void removeReservation(Long id) {
        reservationDao.removeReservationById(id);
    }
}
