package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.response.ReservationTimeAvailableResponse;
import roomescape.dto.response.ReservationTimePostResponse;
import roomescape.entity.ReservationTime;

@Component
public class ReservationTimeQueryService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeQueryService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimePostResponse> findAllTimes() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimePostResponse::new)
                .toList();
    }

    public List<ReservationTimeAvailableResponse> findAvailableTimes(LocalDate date, Long themeId) {
        List<ReservationTime> times = reservationTimeDao.findAll();
        return times.stream()
                .map(time -> {
                    boolean alreadyBooked = reservationDao.existByDateTimeAndTheme(date, time, themeId);
                    return new ReservationTimeAvailableResponse(time, alreadyBooked);
                }).toList();
    }
}
