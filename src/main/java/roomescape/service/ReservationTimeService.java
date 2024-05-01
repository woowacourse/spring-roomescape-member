package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTime insertReservationTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        Long id = reservationTimeDao.insert(
                reservationTimeRequestDto.startAt().format(DateTimeFormatter.ofPattern("HH:mm")));

        return new ReservationTime(id, reservationTimeRequestDto.startAt().format(DateTimeFormatter.ofPattern("HH:mm")));
    }

    public List<ReservationTime> getAllReservationTimes() {
        return reservationTimeDao.findAll();
    }

    public void deleteReservationTime(Long id) {
        if (reservationDao.countByTimeId(id) != 0) {
            throw new IllegalStateException();
        }
        reservationTimeDao.deleteById(id);
    }

    public boolean isBooked(String date, Long timeId, Long themeId) {
        int count = reservationDao.count(date, timeId, themeId);
        return count > 0;
    }
}
