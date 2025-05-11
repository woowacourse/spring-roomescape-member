package roomescape.service.reservationtime;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.reservationtime.ReservationTimeDao;
import roomescape.domain.reservationtime.ReservationTime;
import roomescape.dto.reservationtime.request.ReservationTimeRequestDto;
import roomescape.dto.reservationtime.response.ReservationTimeResponseDto;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponseDto> getAllReservationTimes() {
        return reservationTimeDao.findAllReservationTimes().stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    public ReservationTimeResponseDto saveReservationTime(
            ReservationTimeRequestDto reservationTimeRequestDto) {
        ReservationTime reservationTime = reservationTimeRequestDto.toReservationTime();
        reservationTimeDao.saveReservationTime(reservationTime);
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public void deleteReservationTime(Long id) {
        reservationTimeDao.deleteReservationTime(id);
    }
}
