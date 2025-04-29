package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.TimeDao;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.ReservationTime;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;

@Component
public class ReservationTimeService {

    private TimeDao timeDao;

    public ReservationTimeService(TimeDao timeDao) {
        this.timeDao = timeDao;
    }

    public ReservationTimeResponseDto createTime(ReservationTimeRequestDto timeRequest) {
        ReservationTime reservationTimeWithoutId = timeRequest.toTime();
        long id = timeDao.create(reservationTimeWithoutId);
        ReservationTime reservationTime = reservationTimeWithoutId.copyWithId(new Id(id));
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public List<ReservationTimeResponseDto> findAllTimes() {
        return timeDao.findAll().stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    public void deleteTime(long id) {
        timeDao.deleteById(new Id(id));
    }
}
