package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.model.ReservationTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponseDto> getAllTimes() {
        List<ReservationTime> reservationTimeInfos = reservationTimeDao.findAll();
        return reservationTimeInfos.stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    public ReservationTimeResponseDto saveTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        ReservationTime reservationTime = reservationTimeRequestDto.convertToTime();
        Long id = reservationTimeDao.saveTime(reservationTime);
        return new ReservationTimeResponseDto(id, reservationTime.getStartAt());
    }

    public void deleteTime(Long id) {
        reservationTimeDao.deleteTimeById(id);
    }
}
