package roomescape.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.exception.DuplicateReservationException;
import roomescape.exception.InvalidReferenceException;
import roomescape.exception.PastReservationException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;
import roomescape.repository.ThemeDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ReservationCommandService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    private ReservationTime findTimeReference(long timeId) {
        try {
            return reservationTimeDao.findById(timeId);
        } catch (NoSuchElementException e) {
            throw new InvalidReferenceException("존재하지 않는 예약 시간입니다.");
        }
    }

    private void findThemeReference(long themeId) {
        try {
            themeDao.findById(themeId);
        } catch (NoSuchElementException e) {
            throw new InvalidReferenceException("존재하지 않는 테마입니다.");
        }
    }

    private void validateNotPast(LocalDate date, ReservationTime time) {
        if (date.atTime(time.startAt()).isBefore(LocalDateTime.now())) {
            throw new PastReservationException();
        }
    }

    private void validateDuplicate(LocalDate date, Long timeId, Long themeId) {
        if (reservationDao.existsByDateAndTimeIdAndThemeId(date, timeId, themeId)) {
            throw new DuplicateReservationException();
        }
    }

    public Reservation create(String name, LocalDate date, long timeId, long themeId) {
        ReservationTime time = findTimeReference(timeId);
        findThemeReference(themeId);
        validateNotPast(date, time);
        validateDuplicate(date, timeId, themeId);
        return reservationDao.save(name, date, timeId, themeId);
    }

    public void delete(long reservationId) {
        reservationDao.delete(reservationId);
    }

    public void cancel(long reservationId) {
        Reservation reservation = reservationDao.findById(reservationId);
        validateNotPast(reservation.reservationDate(), reservation.reservationTime());
        reservationDao.delete(reservationId);
    }

    public Reservation update(long reservationId, LocalDate newDate, long newTimeId) {
        ReservationTime newTime = findTimeReference(newTimeId);
        validateNotPast(newDate, newTime);
        Reservation current = reservationDao.findById(reservationId);
        long themeId = current.reservationTheme().id();
        if (reservationDao.existsByDateAndTimeIdAndThemeIdExcluding(newDate, newTimeId, themeId, reservationId)) {
            throw new DuplicateReservationException();
        }
        return reservationDao.updateDateAndTime(reservationId, newDate, newTimeId);
    }
}
