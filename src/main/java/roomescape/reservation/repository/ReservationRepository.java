package roomescape.reservation.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import roomescape.exception.model.RoomEscapeException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.Reservation;
import roomescape.theme.dao.ThemeDao;
import roomescape.theme.domain.Theme;
import roomescape.theme.exception.ThemeExceptionCode;
import roomescape.time.dao.TimeDao;
import roomescape.time.domain.Time;
import roomescape.time.exception.TimeExceptionCode;

@Repository
public class ReservationRepository {

    private final ReservationDao reservationDao;
    private final TimeDao timeDao;
    private final ThemeDao themeDao;

    public ReservationRepository(ReservationDao reservationDao, TimeDao timeDao, ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.timeDao = timeDao;
        this.themeDao = themeDao;
    }

    public Reservation save(Reservation reservation) {
        Time time = timeDao.findById(reservation.getReservationTime().getId())
                .orElseThrow(() -> new RoomEscapeException(TimeExceptionCode.FOUND_TIME_IS_NULL_EXCEPTION));
        Theme theme = themeDao.findById(reservation.getThemeId())
                .orElseThrow(() -> new RoomEscapeException(ThemeExceptionCode.FOUND_THEME_IS_NULL_EXCEPTION));

        reservation.setTimeOnSave(time);
        reservation.setThemeOnSave(theme);

        return reservationDao.save(reservation);
    }

    public List<Reservation> findAllReservationOrderByDateAndTimeStartAt() {
        return reservationDao.findAllReservationOrderByDateAndTimeStartAt();
    }

    public List<Reservation> findAllByThemeIdAndDate(long themeId, LocalDate date) {
        return reservationDao.findAllByThemeIdAndDate(themeId, date);
    }

    public List<Theme> findThemeByDateOrderByThemeIdCountLimit(LocalDate beforeWeek, LocalDate today, int limitCount) {
        return reservationDao.findThemeByDateOrderByThemeIdCountLimit(beforeWeek, today, limitCount);
    }

    public Optional<Reservation> findByTimeId(long timeId) {
        return reservationDao.findByTimeId(timeId);
    }

    public void deleteById(long reservationId) {
        reservationDao.deleteById(reservationId);
    }

    public Optional<Reservation> findByThemeId(long themeId) {
        return reservationDao.findByThemeId(themeId);
    }
}
