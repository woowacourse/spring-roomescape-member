package roomescape.service;

import org.springframework.stereotype.Service;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.entity.ReservationTime;
import roomescape.exception.EntityNotFoundException;
import roomescape.exception.ReservationExistException;
import roomescape.repository.ReservationDao;
import roomescape.repository.ReservationTimeDao;

import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao, ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public ReservationTimeResponseDto add(ReservationTimeRequestDto requestDto) {
        ReservationTime reservationTime = new ReservationTime(requestDto.startAt());
        ReservationTime savedReservationTime = reservationTimeDao.save(reservationTime);
        return createResponseDto(savedReservationTime);
    }

    public List<ReservationTimeResponseDto> findAll() {
        List<ReservationTime> times = reservationTimeDao.findAll();
        return times.stream()
            .map(this::createResponseDto)
            .toList();
    }

    public void deleteById(Long id) {
        if (reservationDao.isExistByTimeId(id)) {
            throw new ReservationExistException("이 시간의 예약이 존재합니다.");
        }

        reservationTimeDao.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("삭제할 예약시간이 없습니다."));

        reservationTimeDao.deleteById(id);
    }

    private ReservationTimeResponseDto createResponseDto(ReservationTime reservationTime) {
        return new ReservationTimeResponseDto(reservationTime.getId(), reservationTime.getStartAt());
    }
}
