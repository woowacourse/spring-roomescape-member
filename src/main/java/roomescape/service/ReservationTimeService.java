package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.ReservationTime;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;

@Component
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponseDto createTime(ReservationTimeRequestDto timeRequest) {
        ReservationTime reservationTimeWithoutId = timeRequest.toTime();
        long id = reservationTimeDao.create(reservationTimeWithoutId);
        ReservationTime reservationTime = reservationTimeWithoutId.copyWithId(new Id(id));
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public List<ReservationTimeResponseDto> findAllTimes() {
        return reservationTimeDao.findAll().stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    public void deleteTime(long id) {
        reservationTimeDao.deleteById(new Id(id));
    }
}
