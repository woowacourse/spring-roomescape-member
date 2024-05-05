package roomescape.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Repository;

import roomescape.controller.request.ReservationRequest;
import roomescape.exception.BadRequestException;
import roomescape.exception.DuplicatedException;
import roomescape.model.Reservation;
import roomescape.model.ReservationTime;
import roomescape.model.Theme;

@Repository
public class ReservationRepository {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;
    private final ThemeDao themeDao;

    public ReservationRepository(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao,
                                 ThemeDao themeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
        this.themeDao = themeDao;
    }

    public Reservation addReservation(ReservationRequest request) {
        ReservationTime reservationTime = reservationTimeDao.findReservationById(request.getTimeId());
        validateReservationDateTimeBeforeNow(request.getDate(), reservationTime.getStartAt());
        validateDuplicatedReservation(request.getDate(), request.getTimeId());

        Theme theme = themeDao.findThemeById(request.getThemeId());

        Reservation reservation = new Reservation(request.getName(), request.getDate(), reservationTime, theme);
        return reservationDao.addReservation(reservation);
    }

    private void validateReservationDateTimeBeforeNow(LocalDate date, LocalTime time) {
        LocalDateTime reservationDateTime = LocalDateTime.of(date, time).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime now = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        if (reservationDateTime.isBefore(now)) {
            throw new BadRequestException("현재(%s) 이전 시간으로 예약할 수 없습니다.".formatted(now));
        }
    }

    private void validateDuplicatedReservation(LocalDate date, Long timeId) {
        Long countReservation = reservationDao.countReservationByDateAndTimeId(date, timeId);
        if (countReservation == null || countReservation > 0) {
            throw new DuplicatedException("이미 해당 시간에 예약이 존재합니다.");
        }
    }

    public List<Reservation> getAllReservations() {
        return reservationDao.getAllReservations();
    }

    public Long countReservationById(Long id) {
        return reservationDao.countReservationById(id);
    }

    public void deleteReservation(Long id) {
        reservationDao.deleteReservation(id);
    }
}
