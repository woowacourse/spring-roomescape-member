package roomescape.reservation.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.message.ExceptionMessage;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.dto.ReservationRequestDto;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(final ReservationDao reservationDao,
                              final ReservationTimeDao reservationTimeDao,
                              final ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation save(final ReservationRequestDto requestDto) {
        final ReservationTime reservationTime = reservationTimeDao.getById(requestDto.timeId());
        final Theme theme = themeDao.getById(requestDto.themeId());
        final Reservation reservation = requestDto.toReservation(reservationTime, theme);

        validateReservationAvailable(reservation);

        final long reservationId = reservationDao.save(reservation);
        return Reservation.createWithId(reservationId, reservation);
    }

    private void validateReservationAvailable(final Reservation reservation) {
        final LocalDate date = reservation.getDate();
        final ReservationTime time = reservation.getTime();
        final Theme theme = reservation.getTheme();
        if (date.isBefore(LocalDate.now())) {
            throw new RoomEscapeException(ExceptionMessage.PAST_DATE_RESERVATION);
        }
        if (date.equals(LocalDate.now()) && time.checkPastTime()) {
            throw new RoomEscapeException(ExceptionMessage.PAST_TIME_RESERVATION);
        }
        boolean isExist = reservationDao.checkExistByReservation(date, time.getId(), theme.getId());
        if (isExist) {
            throw new RoomEscapeException(ExceptionMessage.DUPLICATE_DATE_TIME);
        }
    }

    public void deleteById(final long id) {
        reservationDao.deleteById(id);
    }
}
