package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
        return reservationDao.selectById(id);
    }

    @Transactional
    public Reservation add(String name, Long themeId, LocalDate date, Long timeId) {
        ReservationTime time = timeDao.selectById(timeId);
        List<Reservation> reservedList = reservationDao.selectByThemeIdAndDate(themeId, date);

        validateDateTime(date, time);
        for (Reservation reserved : reservedList) {
            validateReserved(timeId, reserved.getTime());
        }

        Reservation newReservation = new Reservation(name, themeId, date, time);
        return reservationDao.insert(newReservation);
    }

    public void delete(Long id) {
        reservationDao.delete(id);
    }

    private void validateReserved(Long timeId, ReservationTime reservedTime) {
        if (timeId.equals(reservedTime.getId())) {
            throw new IllegalArgumentException("[ERROR] 이미 예약이 존재합니다.");
        }
    }

    private void validateDateTime(LocalDate date, ReservationTime time) {
        if (isBeforeDateTime(date, time)) {
            throw new IllegalArgumentException("[ERROR] 지난 시간은 예약할 수 없습니다.");
        }
    }

    private boolean isBeforeDateTime(LocalDate date, ReservationTime time) {
        if (date.isBefore(LocalDate.now())) {
            return true;
        }
        return date.equals(LocalDate.now()) && time.getStartAt().isBefore(LocalTime.now());
    }
}
