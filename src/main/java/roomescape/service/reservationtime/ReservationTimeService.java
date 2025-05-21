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

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponseDto> getAllReservationTimes() {
        return reservationTimeDao.findAllReservationTimes().stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    public ReservationTimeResponseDto saveReservationTime(final ReservationTimeRequestDto request) {
        final ReservationTime reservationTime = request.toReservationTime();
        reservationTimeDao.saveReservationTime(reservationTime);

        return ReservationTimeResponseDto.from(reservationTime);
    }

    public void deleteReservationTime(final Long id) {
        reservationTimeDao.deleteReservationTime(id);
    }
}
