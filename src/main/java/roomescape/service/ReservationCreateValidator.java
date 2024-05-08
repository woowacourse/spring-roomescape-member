package roomescape.service;

import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.reservation.Reservation;
import roomescape.domain.reservation.ReservationDate;
import roomescape.domain.reservation.ReservationTime;
import roomescape.domain.reservation.Theme;
import roomescape.exception.AlreadyExistsException;
import roomescape.exception.NotExistException;
import roomescape.exception.PastTimeReservationException;
import roomescape.service.dto.input.ReservationInput;
import roomescape.service.util.DateTimeFormatter;

import static roomescape.exception.ExceptionDomainType.*;

@Component
public class ReservationCreateValidator {
    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final DateTimeFormatter dateTimeFormatter;


    public ReservationCreateValidator(final ReservationDao reservationDao, final ReservationTimeDao reservationTimeDao, final ThemeDao themeDao, final DateTimeFormatter dateTimeFormatter) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public Reservation validateReservationInput(final ReservationInput input) {
        final ReservationTime reservationTime = validateExistReservationTime(input.timeId());
        final Theme theme = validateExistTheme(input.themeId());

        final Reservation reservation = input.toReservation(reservationTime, theme);
        if (reservationDao.isExistByReservationAndTime(ReservationDate.from(input.date()), input.timeId())) {
            throw new AlreadyExistsException(RESERVATION, reservation.getLocalDateTimeFormat());
        }
        if (reservation.isBefore(dateTimeFormatter.getDate(), dateTimeFormatter.getTime())) {
            throw new PastTimeReservationException(reservation.getLocalDateTimeFormat());
        }
        return reservation;
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
