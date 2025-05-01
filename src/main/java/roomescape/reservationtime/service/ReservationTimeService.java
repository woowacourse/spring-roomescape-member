package roomescape.reservationtime.service;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.exception.ExistedReservationException;
import roomescape.exception.ReservationTimeNotFoundException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservationtime.ReservationTime;
import roomescape.reservationtime.dao.ReservationTimeDao;
import roomescape.reservationtime.dto.request.ReservationTimeRequest;
import roomescape.reservationtime.dto.response.AvailableTimeResponse;
import roomescape.reservationtime.dto.response.ReservationTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTimeResponse> findAll() {
        List<ReservationTime> reservationTimeDaoAll = reservationTimeDao.findAll();

        return reservationTimeDaoAll.stream()
                .map(time -> {
                    return ReservationTimeResponse.toDto(time);
                })
                .toList();
    }

    public Long create(@Valid @RequestBody ReservationTimeRequest reservationTimeRequest) {
        ReservationTime reservationTime = reservationTimeRequest.toTime();
        return reservationTimeDao.create(reservationTime);
    }

    public void delete(@PathVariable Long id) {
        if (reservationTimeDao.findById(id).isEmpty()) {
            throw new ReservationTimeNotFoundException();
        }
        if (reservationDao.findByTimeId(id).isPresent()) {
            throw new ExistedReservationException();
        }
        reservationTimeDao.delete(id);
    }

    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, Long themeId) {
        List<ReservationTime> nonAvailableReservations = reservationDao.findAll().stream()
                .filter(reservation -> reservation.getDate().equals(date))
                .filter(reservation -> reservation.getTheme().getId().equals(themeId))
                .map(reservation -> reservation.getReservationTime())
                .toList();

        List<ReservationTime> timeDaoAll = reservationTimeDao.findAll();

        return timeDaoAll.stream()
                .map(t -> new AvailableTimeResponse(t.getId(), t.getStartAt(), nonAvailableReservations.contains(t)))
                .toList();
    }
}
