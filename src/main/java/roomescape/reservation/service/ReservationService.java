package roomescape.reservation.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.member.domain.Member;
import roomescape.reservation.domain.Reservation;
import roomescape.reservation.domain.ReservationDateTime;
import roomescape.reservation.repository.ReservationDao;
import roomescape.reservation.request.ReservationRequest;
import roomescape.theme.domain.Theme;
import roomescape.theme.repository.ThemeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.repository.ReservationTimeDao;

@Service
public class ReservationService {
    private static final int DELETE_SUCCESS = 1;
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

    public Reservation save(ReservationRequest request) {
        validateDuplicate(request);
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        Theme theme = themeDao.findById(request.themeId());
        return reservationDao.save(new Reservation(request.name(), request.date(), reservationTime, theme));
    }

    public Reservation validatePastAndSave(ReservationRequest request, Member member) {
        validateDuplicate(request);
        ReservationTime reservationTime = reservationTimeDao.findById(request.timeId());
        new ReservationDateTime(request, reservationTime).validatePast(LocalDateTime.now());
        Theme theme = themeDao.findById(request.themeId());
        return reservationDao.save(new Reservation(member.name(), request.date(), reservationTime, theme));

    }

    private void validateDuplicate(ReservationRequest request) {
        if (reservationDao.isDuplicate(request.date(), request.timeId(), request.themeId())) {
            throw new IllegalArgumentException("Reservation already exists");
        }
    }

    public void delete(long id) {
        if (reservationDao.deleteById(id) != DELETE_SUCCESS) {
            throw new IllegalArgumentException("Cannot delete a reservation by given id");
        }
    }

}
