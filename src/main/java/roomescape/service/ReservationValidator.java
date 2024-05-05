package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationDate;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.AlreadyExistsException;
import roomescape.exception.NotExistException;
import roomescape.service.dto.input.ReservationInput;

import static roomescape.exception.ExceptionDomainType.*;

@Component
public class ReservationValidator {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationValidator(final ReservationDao reservationDao, final ReservationTimeDao reservationTimeDao, final ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public Reservation validateReservationInput(final ReservationInput input) {
        final ReservationTime reservationTime = validateExistReservationTime(input.timeId());
        final Theme theme = validateExistTheme(input.themeId());

        if (reservationDao.isExistByReservationAndTime(ReservationDate.from(input.date()), input.timeId())) {
            throw new AlreadyExistsException(RESERVATION, input.date() + reservationTime.getStartAtAsString());
        }

        return input.toReservation(reservationTime, theme);
    }

    private ReservationTime validateExistReservationTime(final long timeId) {
        return reservationTimeDao.find(timeId)
                                 .orElseThrow(() -> new NotExistException(RESERVATION_TIME, timeId));
    }

    private Theme validateExistTheme(final long themeId) {
        return themeDao.find(themeId)
                       .orElseThrow(() -> new NotExistException(THEME, themeId));
    }
}
