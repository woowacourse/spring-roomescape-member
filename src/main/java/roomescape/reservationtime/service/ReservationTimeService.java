package roomescape.reservationtime.service;

import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import roomescape.exception.ExistedException;
import roomescape.exception.ReservationNotFoundException;
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
        ReservationTime reservationTime = ReservationTime.createWithoutId(reservationTimeRequest.startAt());
        return reservationTimeDao.create(reservationTime);
    }

    public void delete(@PathVariable Long id) {
        if (reservationTimeDao.findById(id).isEmpty()) {
            throw new ReservationNotFoundException("예약 시간이 존재하지 않습니다.");
        }
        if (reservationDao.findByTimeId(id).isPresent()) {
            throw new ExistedException("예약이 이미 존재합니다.");
        }
        reservationTimeDao.delete(id);
    }

    public List<AvailableTimeResponse> findAvailableTimes(LocalDate date, Long themeId) {
        List<ReservationTime> availableReservationTimes = reservationDao.findAvailableTimesByDateAndThemeId(date, themeId);

        List<ReservationTime> timeDaoAll = reservationTimeDao.findAll();

        return timeDaoAll.stream()
                .map(t -> new AvailableTimeResponse(t.getId(), t.getStartAt(), !availableReservationTimes.contains(t)))
                .toList();
    }
}
