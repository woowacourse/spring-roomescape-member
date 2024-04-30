package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.ReservationTimeRequestDto;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
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
        reservationTimeDao.deleteById(id);
    }
}
