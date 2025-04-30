package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationTimeDao;
import roomescape.dto.AvailableReservationTimeResponseDto;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.model.AvailableReservationTime;
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
        LocalTime parsedStartAt = LocalTime.parse(reservationTimeRequestDto.startAt());
        boolean duplicatedStartAtExisted = reservationTimeDao.isDuplicatedStartAtExisted(parsedStartAt);
        if (duplicatedStartAtExisted) {
            throw new IllegalStateException("중복된 예약시각은 등록할 수 없습니다.");
        }

        ReservationTime reservationTime = reservationTimeRequestDto.convertToTime();
        Long id = reservationTimeDao.saveTime(reservationTime);

        return new ReservationTimeResponseDto(id, reservationTime.getStartAt());
    }

    public void deleteTime(final Long id) {
        try {
            reservationTimeDao.deleteTimeById(id);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("삭제하고자 하는 시각에 예약된 정보가 있습니다.");
        }
    }

    public List<AvailableReservationTimeResponseDto> getAvailableTimes(String date, Long themeId) {
        List<AvailableReservationTime> availableTimes = reservationTimeDao.findAvailableTimes(date, themeId);
        return availableTimes.stream().map(AvailableReservationTimeResponseDto::from).toList();
    }
}
