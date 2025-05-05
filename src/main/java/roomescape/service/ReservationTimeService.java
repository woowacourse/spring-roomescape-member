package roomescape.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.dao.ReservationDao;
import roomescape.dao.ReservationTimeDao;
import roomescape.domain.ReservationTime;
import roomescape.dto.BookedReservationTimeResponseDto;
import roomescape.dto.ReservationTimeRequestDto;
import roomescape.dto.ReservationTimeResponseDto;
import roomescape.exception.InvalidReservationException;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(ReservationDao reservationDao,
        ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
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
        long savedId = reservationTimeDao.saveReservationTime(reservationTime);
        reservationTime.setId(savedId);
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public void deleteReservationTime(Long id) {
        if (reservationDao.findByTimeId(id) != 0) {
            throw new InvalidReservationException("이미 예약된 예약 시간을 삭제할 수 없습니다.");
        }
        reservationTimeDao.deleteReservationTime(id);
    }

    public List<BookedReservationTimeResponseDto> getTimesContainsReservationInfoBy(String date,
        Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAllReservationTimes();

        return reservationTimes.stream()
            .map(reservationTime -> createBookedReservationTimeResponseDto(date, themeId,
                reservationTime))
            .toList();
    }

    private BookedReservationTimeResponseDto createBookedReservationTimeResponseDto(
        String date, Long themeId, ReservationTime reservationTime) {
        if (isAlreadyBookedTime(date, themeId, reservationTime)) {
            return BookedReservationTimeResponseDto.from(reservationTime, true);
        }
        return BookedReservationTimeResponseDto.from(reservationTime, false);
    }

    private boolean isAlreadyBookedTime(String date, Long themeId,
        ReservationTime reservationTime) {
        int alreadyExistReservationCount = reservationDao.calculateAlreadyExistReservationBy(
            date, themeId, reservationTime.getId());
        return alreadyExistReservationCount != 0;
    }
}
