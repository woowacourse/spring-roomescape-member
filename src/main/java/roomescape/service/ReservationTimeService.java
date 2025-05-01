package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain_entity.ReservationTime;
import roomescape.dto.ReservationTimeAvailableResponse;
import roomescape.dto.ReservationTimeRequest;
import roomescape.dto.ReservationTimeResponse;

@Component
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponse createTime(ReservationTimeRequest timeRequest) {
        ReservationTime reservationTimeWithoutId = timeRequest.toTime();
        long id = reservationTimeDao.create(reservationTimeWithoutId);
        ReservationTime reservationTime = reservationTimeWithoutId.copyWithId(id);
        return ReservationTimeResponse.from(reservationTime);
    }

    public List<ReservationTimeResponse> findAllTimes() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponse::from)
                .toList();
    }

    public void deleteTime(long id) {
        Boolean hasTime = reservationDao.existByTimeId(id);

        if (hasTime) {
            throw new IllegalArgumentException("예약 시간에 존재하는 예약 정보가 있습니다.");
        }

        reservationTimeDao.deleteById(id);
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
