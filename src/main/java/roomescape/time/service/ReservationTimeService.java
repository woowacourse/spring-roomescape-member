package roomescape.time.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import roomescape.response.ResponseCode;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationUserTime;
import roomescape.time.dto.ReservationTimeRequestDto;
import roomescape.time.dto.ReservationTimeResponseDto;

import java.util.List;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(final ReservationTimeDao reservationTimeDao) {
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTimeResponseDto> findAll() {
        final List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        return reservationTimes.stream()
                .map(ReservationTimeResponseDto::new)
                .toList();
    }

    public ReservationTimeResponseDto save(final ReservationTimeRequestDto requestDto) {
        final long id = reservationTimeDao.save(requestDto.toReservationTime());
        final ReservationTime reservationTime = reservationTimeDao.findById(id);
        return new ReservationTimeResponseDto(id, reservationTime.getStartAt().toString());
    }

    public ResponseCode deleteById(final long id) {
        try {
            if (reservationTimeDao.deleteById(id) > 0) {
                return ResponseCode.SUCCESS_DELETE;
            }
            return ResponseCode.NOT_FOUND;
        } catch (final DataAccessException dataAccessException) {
            return ResponseCode.FAILED_DELETE;
        }
    }

    public List<ReservationUserTime> findAvailableTime(final String date, final long themeId) {
        return reservationTimeDao.findAvailableTime(date, themeId);
    }
}
