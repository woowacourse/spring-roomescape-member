package roomescape.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dao.ThemeDao;
import roomescape.domain.Reservation;
import roomescape.domain.ReservationTime;
import roomescape.domain.Theme;
import roomescape.exception.DuplicatedResourceException;
import roomescape.exception.PastResourceAccessException;
import roomescape.exception.ResourceNotFoundException;

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
        if (reservationDao.hasDuplicateReservation(date, timeId, themeId)) {
            throw new DuplicatedResourceException("이미 존재하는 예약입니다.", "DUPLICATED_RESERVATION");
        }
        ReservationTime time = reservationTimeDao.findById(timeId);
        validatePastReservation(date, time, "예약");

        Theme theme = themeDao.findById(themeId);
        Reservation reservation = new Reservation(name, date, time, theme);
        return reservationDao.save(reservation);
    }

    public List<Reservation> findByName(String name) {
        return reservationDao.findByName(name);
    }

    public Reservation update(Long id, LocalDate newDate, Long newTimeId, Long newThemeId) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("예약이 존재하지 않습니다", "RESERVATION_NOT_FOUND"));
        validatePastReservation(reservation.date(), reservation.time(), "변경");

        ReservationTime reservationTime = reservationTimeDao.findById(newTimeId);
        validatePastReservation(newDate, reservationTime, "변경");
        validateHasDuplicateReservation(newDate, newTimeId, newThemeId, id);
        reservationDao.updateReservation(id, newDate, newTimeId, newThemeId);

        return reservationDao.findById(id).orElseThrow();
    }

    public void deleteByIdFromAdmin(Long id) {
        validateHasReservation(id);
        reservationDao.deleteById(id);
    }

    public void deleteByIdFromMember(Long id) {
        Reservation reservation = reservationDao.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("예약이 존재하지 않습니다", "RESERVATION_NOT_FOUND"));
        validatePastReservation(reservation.date(), reservation.time(), "삭제");
        reservationDao.deleteById(id);
    }

    private boolean isTodayButBeforeTime(LocalDate date, ReservationTime time) {
        return date.isEqual(LocalDate.now()) && time.startAt().isBefore(LocalTime.now());
    }

    private void validateHasDuplicateReservation(LocalDate newDate, Long newTimeId, Long newThemeId, Long id) {
        if (reservationDao.hasDuplicateReservationForUpdate(newDate, newTimeId, newThemeId, id)) {
            throw new DuplicatedResourceException("이미 존재하는 예약입니다.", "DUPLICATED_RESERVATION");
        }
    }

    private void validateHasReservation(Long id) {
        boolean hasReservation = reservationDao.existsById(id);
        if (!hasReservation) {
            throw new ResourceNotFoundException("존재하지 않는 예약이라 삭제할 수 없습니다.", "RESOURCE_NOT_FOUND");
        }
    }

    private void validatePastReservation(LocalDate date, ReservationTime reservationTime, String action) {
        if (date.isBefore(LocalDate.now()) || isTodayButBeforeTime(date, reservationTime)) {
            throw new PastResourceAccessException("과거 예약은 " + action + "할 수 없습니다.", "INVALID_PAST_RESERVATION_ACCESS");
        }
    }
}
