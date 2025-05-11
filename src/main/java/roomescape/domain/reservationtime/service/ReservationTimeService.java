package roomescape.domain.reservationtime.service;

import java.util.List;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.reservationtime.dao.ReservationTimeDao;
import roomescape.domain.reservationtime.dto.request.ReservationTimeRequestDto;
import roomescape.domain.reservationtime.dto.response.BookedReservationTimeResponseDto;
import roomescape.domain.reservationtime.dto.response.ReservationTimeResponseDto;
import roomescape.domain.reservationtime.model.ReservationTime;
import roomescape.global.exception.InvalidReservationException;

@Service
public class ReservationTimeService {

    private final ReservationTimeDao reservationTimeDao;
    private final ReservationDao reservationDao;

    public ReservationTimeService(
        ReservationTimeDao reservationTimeDao,
        ReservationDao reservationDao) {
        this.reservationTimeDao = reservationTimeDao;
        this.reservationDao = reservationDao;
    }

    public List<ReservationTimeResponseDto> getAllReservationTimes() {
        return reservationTimeDao.findAll().stream()
            .map(ReservationTimeResponseDto::from)
            .toList();
    }

    public ReservationTimeResponseDto saveReservationTime(
        ReservationTimeRequestDto reservationTimeRequestDto) {
        ReservationTime reservationTime = reservationTimeRequestDto.toReservationTime();
        long savedId = reservationTimeDao.save(reservationTime);
        reservationTime.setId(savedId);
        return ReservationTimeResponseDto.from(reservationTime);
    }

    public void deleteReservationTime(Long id) {
        if (reservationDao.existReservationByTime(id)) {
            throw new InvalidReservationException("이미 예약된 예약 시간을 삭제할 수 없습니다.");
        }
        boolean isDeleted = reservationTimeDao.delete(id);
        if (!isDeleted) {
            throw new IllegalArgumentException("해당 ID의 예약시간을 찾을 수 없습니다");
        }
    }

    public void findById(Long id) {
        reservationTimeDao.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 ID의 예약시간을 찾을 수 없습니다"));
    }

    public List<BookedReservationTimeResponseDto> getTimesContainsReservationInfoBy(String date,
        Long themeId) {
        return reservationTimeDao.findBooked(date, themeId);
    }
}
