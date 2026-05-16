package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.PastReservationException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationCommandService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    private void validatePastDateTime(LocalDate date, long timeId) {
        ReservationTime reservationTime = reservationTimeDao.findById(timeId);
        if (date.atTime(reservationTime.startAt()).isBefore(LocalDateTime.now())) {
            throw new PastReservationException();
        }
    }

    private void validateDuplicate(LocalDate date, Long timeId, Long themeId) {
        if (reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new DuplicateReservationException();
        }
    }

    public Reservation create(String name, LocalDate date, long timeId, long themeId) {
        validatePastDateTime(date, timeId);
        validateDuplicate(date, timeId, themeId);
        return reservationDao.save(name, date, timeId, themeId);
    }

    public void delete(long reservationId) {
        reservationDao.delete(reservationId);
    }

    public void cancel(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        if (reservation.reservationDate().atTime(reservation.reservationTime().startAt()).isBefore(LocalDateTime.now())) {
            throw new PastReservationException();
        }
        reservationDao.delete(reservationId);
    }

    public Reservation update(long reservationId, LocalDate newDate, long newTimeId) {
        validatePastDateTime(newDate, newTimeId);
        Reservation current = reservationDao.findById(reservationId);
        long themeId = current.reservationTheme().id();
        if (reservationDao.existsByDateAndTimeIdAndThemeIdExcluding(newDate, newTimeId, themeId, reservationId)) {
            throw new DuplicateReservationException();
        }
        return reservationDao.updateDateAndTime(reservationId, newDate, newTimeId);
    }
}
