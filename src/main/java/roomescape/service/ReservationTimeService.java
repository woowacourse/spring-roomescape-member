package roomescape.service;

import java.time.LocalDate;
import java.util.List;
import org.springframework.stereotype.Component;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain_entity.Id;
import roomescape.domain_entity.ReservationTime;
import roomescape.dto.ReservationTimeAvailableResponse;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;

@Component
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
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
        Boolean hasTime = reservationDao.existByTimeId(new Id(id));

        if (hasTime) {
            throw new IllegalArgumentException("예약 시간에 존재하는 예약 정보가 있습니다.");
        }

        reservationTimeDao.deleteById(new Id(id));
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
