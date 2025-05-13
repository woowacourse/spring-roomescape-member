package roomescape.service;

import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.common.exception.DuplicatedException;
import roomescape.common.exception.ResourceInUseException;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.controller.reservationTime.dto.AvailableReservationTimeResponseDto;
import roomescape.controller.reservationTime.dto.ReservationTimeRequestDto;
import roomescape.controller.reservationTime.dto.ReservationTimeResponseDto;
import roomescape.model.AvailableReservationTime;
import roomescape.model.ReservationTime;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(ReservationTimeDao reservationTimeDao, ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTimeResponseDto> getAllTimes() {
        List<ReservationTime> reservationTimeInfos = reservationTimeDao.findAll();
        return reservationTimeInfos.stream()
                .map(ReservationTimeResponseDto::from)
                .toList();
    }

    public ReservationTimeResponseDto saveTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        validateReservationTime(reservationTimeRequestDto);

        ReservationTime reservationTime = reservationTimeRequestDto.convertToTime();
        Long id = reservationTimeDao.saveTime(reservationTime);

        return new ReservationTimeResponseDto(id, reservationTime.getStartAt());
    }

    public void deleteTime(final Long id) {
        boolean isReserved = reservationDao.existsByReservationTimeId(id);
        if (isReserved) {
            throw new ResourceInUseException("삭제하고자 하는 시각에 예약된 정보가 있습니다.");
        }
        reservationTimeDao.deleteTimeById(id);
    }

    public List<AvailableReservationTimeResponseDto> getAvailableTimes(String date, Long themeId) {
        List<AvailableReservationTime> availableTimes = reservationTimeDao.findAvailableTimes(date, themeId);
        return availableTimes.stream().map(AvailableReservationTimeResponseDto::from).toList();
    }

    private void validateReservationTime(ReservationTimeRequestDto reservationTimeRequestDto) {
        LocalTime parsedStartAt = reservationTimeRequestDto.startAt();
        boolean duplicatedStartAtExisted = reservationTimeDao.isDuplicatedStartAtExisted(parsedStartAt);
        if (duplicatedStartAtExisted) {
            throw new DuplicatedException("중복된 예약시각은 등록할 수 없습니다.");
        }
    }
}