package roomescape.domain.reservationtime.service;

import static roomescape.global.exception.ErrorMessage.ALREADY_USED_RESOURCE;
import static roomescape.global.exception.ErrorMessage.NOT_FOUND_ID;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import roomescape.domain.reservation.dao.ReservationDao;
import roomescape.domain.reservationtime.dao.ReservationTimeDao;
import roomescape.domain.reservationtime.dto.request.ReservationTimeRequestDto;
import roomescape.domain.reservationtime.dto.response.BookedReservationTimeResponseDto;
import roomescape.domain.reservationtime.dto.response.ReservationTimeResponseDto;
import roomescape.domain.reservationtime.model.ReservationTime;
import roomescape.global.exception.InvalidReservationException;
import roomescape.global.exception.NotFoundException;

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
            throw new InvalidReservationException(ALREADY_USED_RESOURCE);
        }
        boolean isDeleted = reservationTimeDao.delete(id);
        if (!isDeleted) {
            throw new NotFoundException(NOT_FOUND_ID);
        }
    }

    public List<BookedReservationTimeResponseDto> getTimesContainsReservationInfoBy(
        String date, Long themeId) {
        List<ReservationTime> reservationTimes = reservationTimeDao.findAll();
        List<ReservationTime> bookedTimes = reservationTimeDao.findBookedTimes(date, themeId);
        return reservationTimes.stream()
            .map(time -> BookedReservationTimeResponseDto.from(time, bookedTimes.contains(time)))
            .collect(Collectors.toList());
    }
}
