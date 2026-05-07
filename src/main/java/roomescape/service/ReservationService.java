package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public List<Reservation> findAll() {
        return reservationDao.findAll();
    }

    public Reservation save(String name, LocalDate date, Long timeId, Long themeId) {
        if (reservationDao.existByDateAndTimeIAndThemeId(date, timeId, themeId)) {
            throw new IllegalArgumentException("이미 존재하는 예약입니다.");
        }
        ReservationTime time = reservationTimeDao.findById(timeId);

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("지난 날짜는 예약할 수 없습니다.");
        }
        if (date.isEqual(LocalDate.now()) && time.getStartAt().isBefore(java.time.LocalTime.now())) {
            throw new IllegalArgumentException("지난 시간은 예약할 수 없습니다.");
        }

        Theme theme = themeDao.findById(themeId);
        Reservation reservation = new Reservation(name, date, time, theme);
        return reservationDao.save(reservation);
    }

    public void deleteById(Long id) {
        validateHasReservation(id);
        reservationDao.deleteById(id);
    }

    private void validateHasReservation(Long id) {
        boolean hasReservation = reservationDao.existById(id);
        if (!hasReservation) {
            throw new IllegalArgumentException("존재하지 않는 예약이라 삭제할 수 없습니다.");
        }
    }
}
