package roomescape.service;

import java.util.List;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.*;
import roomescape.service.dto.input.ReservationInput;
import roomescape.service.dto.output.ReservationOutput;
import roomescape.service.util.DateTimeFormatter;

import static roomescape.exception.ExceptionDomainType.*;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;
    private final DateTimeFormatter dateTimeFormatter;

    public ReservationService(final ReservationDao reservationDao,
                              final ReservationTimeDao reservationTimeDao,
                              final ThemeDao themeDao,
                              final DateTimeFormatter dateTimeFormatter) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
        this.dateTimeFormatter = dateTimeFormatter;
    }

    public ReservationOutput createReservation(final ReservationInput input) {
        final ReservationTime time = reservationTimeDao.find(input.timeId())
                                                       .orElseThrow(() -> new NotExistException(RESERVATION_TIME, input.timeId()));

        final Theme theme = themeDao.find(input.themeId())
                                    .orElseThrow(() -> new NotExistException(THEME, input.themeId()));

        final Reservation reservation = input.toReservation(time, theme);
        if (reservationDao.isExistByReservationAndTime(reservation.getDate(), time.getId())) {
            throw new AlreadyExistsException(RESERVATION, reservation.getDateAndTimeFormat());
        }
        if (reservation.isBefore(dateTimeFormatter.getDate(), dateTimeFormatter.getTime())) {
            throw new PastTimeReservationException(reservation.getDateAndTimeFormat());
        }
        final Reservation savedReservation = reservationDao.create(reservation);
        return ReservationOutput.toOutput(savedReservation);
    }

    public List<ReservationOutput> getAllReservations() {
        final List<Reservation> reservations = reservationDao.getAll();
        return ReservationOutput.toOutputs(reservations);
    }

    public void deleteReservation(final long id) {
        if (!reservationDao.isExistById(id)) {
            throw new NotExistException(RESERVATION, id);
        }
        reservationDao.delete(id);
    }
}
