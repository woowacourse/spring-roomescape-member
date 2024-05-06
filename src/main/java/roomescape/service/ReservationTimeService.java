package roomescape.service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.time.TimeRequest;
import roomescape.dto.time.TimeResponse;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public TimeResponse insertReservationTime(TimeRequest timeRequest) {
        Long id = reservationTimeDao.insert(
                timeRequest.startAt().format(DateTimeFormatter.ofPattern("HH:mm")));
        ReservationTime inserted = new ReservationTime(id,
                timeRequest.startAt());

        return new TimeResponse(inserted);
    }

    public List<TimeResponse> getAllReservationTimes() {
        return reservationTimeDao.findAll().stream()
                .map(TimeResponse::new)
                .toList();
    }

    public void deleteReservationTime(Long id) {
        if (reservationDao.hasTime(id)) {
            throw new IllegalStateException("예약이 존재하는 시간은 삭제할 수 없습니다.");
        }
        reservationTimeDao.deleteById(id);
    }

    public boolean isBooked(String date, Long timeId, Long themeId) {
        int count = reservationDao.count(date, timeId, themeId);
        return count > 0;
    }
}
