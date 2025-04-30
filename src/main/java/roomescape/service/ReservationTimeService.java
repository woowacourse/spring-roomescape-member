package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.request.ReservationTimeRequest;
import roomescape.dto.response.ReservationAvailableTimeResponse;
import roomescape.dto.response.ReservationTimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao,
        ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public ReservationTimeResponse create(ReservationTimeRequest request) {
        ReservationTime reservationTime = new ReservationTime(request.startAt());
        reservationTime = reservationTimeDao.save(reservationTime);
        return ReservationTimeResponse.from(reservationTime);
    }

    public List<ReservationTimeResponse> findAll() {
        return reservationTimeDao.findAll()
            .stream()
            .map(ReservationTimeResponse::from)
            .toList();
    }

    public boolean deleteReservationTime(Long id) {
        int deleteCount = reservationTimeDao.deleteById(id);
        return deleteCount != 0;
    }

    public List<ReservationAvailableTimeResponse> findAvailableTimes(Long themeId, LocalDate date) {
        List<ReservationTime> times = reservationTimeDao.findAll();

        return times.stream()
            .map(time -> {
                Long timeId = time.getId();
                boolean isBooked = (reservationDao.getCountByTimeIdAndThemeIdAndDate(timeId, themeId, date) != 0);
                return new ReservationAvailableTimeResponse(timeId, time.getStartAt(), isBooked);
            }).toList();
    }
}
