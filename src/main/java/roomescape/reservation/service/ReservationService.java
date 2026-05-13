package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import roomescape.global.exception.ErrorCode;
import roomescape.global.exception.RoomescapeException;
import roomescape.reservation.Reservation;
import roomescape.reservation.dao.ReservationDao;
import roomescape.time.ReservationTime;
import roomescape.time.dao.TimeDao;

@Service
public class ReservationService {
    private final ReservationDao reservationDao;
    private final TimeDao timeDao;

    public ReservationService(ReservationDao reservationDao, TimeDao timeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.selectAll();
    }

    public Reservation findById(Long id) {
        return reservationDao.selectById(id)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    public List<Reservation> findByName(String name) {
        return reservationDao.selectByName(name);
    }

    @Transactional
    public Reservation add(String name, Long themeId, LocalDate date, Long timeId) {
        ReservationTime time = timeDao.selectById(timeId);
        validateDateTime(date, time);

        List<Reservation> reservedList = reservationDao.selectByThemeIdAndDate(themeId, date);
        for (Reservation reserved : reservedList) {
            validateReserved(timeId, reserved.getTime());
        }

        Reservation newReservation = new Reservation(name, themeId, date, time);
        return reservationDao.insert(newReservation);
    }

    @Transactional
    public Reservation modifyDateTimeByName(Long id, String name, Long themeId, LocalDate date, Long timeId) {
        Reservation originReservation = reservationDao.selectById(id)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND));

        if (!originReservation.getName().equals(name)) {
            throw new RoomescapeException(ErrorCode.CANNOT_MODIFY_OTHER_RESERVATION);
        }

        ReservationTime time = timeDao.selectById(timeId);
        validateDateTime(date, time);

        List<Reservation> reservedList = reservationDao.selectByThemeIdAndDate(themeId, date);
        for (Reservation reserved : reservedList) {
            validateReserved(timeId, reserved.getTime());
        }

        return reservationDao.updateDateTimeById(id, date, timeId)
                .orElseThrow(() -> new RoomescapeException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    public void delete(Long id) {
        reservationDao.delete(id);
    }

    private void validateReserved(Long timeId, ReservationTime reservedTime) {
        if (timeId.equals(reservedTime.getId())) {
            throw new RoomescapeException(ErrorCode.RESERVATION_ALREADY_EXISTS);
        }
    }

    private void validateDateTime(LocalDate date, ReservationTime time) {
        if (isBeforeDateTime(date, time)) {
            throw new RoomescapeException(ErrorCode.PAST_RESERVATION);
        }
    }

    private boolean isBeforeDateTime(LocalDate date, ReservationTime time) {
        if (date.isBefore(LocalDate.now())) {
            return true;
        }
        return date.equals(LocalDate.now()) && time.getStartAt().isBefore(LocalTime.now());
    }
}
