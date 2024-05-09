package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.ExistsException;
import roomescape.exception.InvalidInputException;
import roomescape.exception.NotExistsException;
import roomescape.service.dto.input.ReservationInput;
import roomescape.service.dto.output.ReservationOutput;
import roomescape.service.util.DateTimeFormatter;

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
                .orElseThrow(() -> NotExistsException.of("timeId", input.timeId()));
        final Theme theme = themeDao.find(input.themeId())
                .orElseThrow(() -> NotExistsException.of("themeId", input.themeId()));

        final Reservation reservation = input.toReservation(time, theme);
        if (reservationDao.isExistByReservationAndTime(reservation.getDate(), time.getId())) {
            throw ExistsException.of(String.format("date 가 %s 이고 reservationTimeId 가 %d인 reservation", reservation.getDate(), reservation.getId()));
        }
        if (reservation.isBefore(dateTimeFormatter.getDate(), dateTimeFormatter.getTime())) {
            throw InvalidInputException.of(
                    String.format("date 와 time (%s %s 이후만 가능)", dateTimeFormatter.getDate(), dateTimeFormatter.getTime().format(
                            java.time.format.DateTimeFormatter.ofPattern("hh:mm"))),
                    String.format("%s", reservation.getDateAndTimeFormat())
            );
        }
        final Reservation savedReservation = reservationDao.create(reservation);
        return ReservationOutput.from(savedReservation);
    }

    public List<ReservationOutput> getAllReservations() {
        final List<Reservation> reservations = reservationDao.getAll();
        return ReservationOutput.list(reservations);
    }

    public void deleteReservation(final long id) {
        if (!reservationDao.isExistById(id)) {
            throw NotExistsException.of("reservationId", id);
        }
        reservationDao.delete(id);
    }
}
