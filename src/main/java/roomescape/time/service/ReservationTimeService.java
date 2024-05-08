package roomescape.time.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.exception.RoomEscapeException;
import roomescape.exception.message.ExceptionMessage;
import roomescape.reservation.dao.ReservationDao;
import roomescape.time.dao.ReservationTimeDao;
import roomescape.time.domain.ReservationTime;
import roomescape.time.domain.ReservationUserTime;
import roomescape.time.dto.ReservationTimeRequestDto;

@Service
public class ReservationTimeService {

    private final ReservationDao reservationDao;
    private final ReservationTimeDao reservationTimeDao;

    public ReservationTimeService(final ReservationDao reservationDao, final ReservationTimeDao reservationTimeDao) {
        this.reservationDao = reservationDao;
        this.reservationTimeDao = reservationTimeDao;
    }

    public List<ReservationTime> findAll() {
        return reservationTimeDao.findAll();
    }

    public ReservationTime save(final ReservationTimeRequestDto requestDto) {
        final ReservationTime reservationTime = new ReservationTime(requestDto.startAt());
        if (reservationTimeDao.checkExistTime(reservationTime)) {
            throw new RoomEscapeException(ExceptionMessage.DUPLICATE_TIME);
        }
        final long id = reservationTimeDao.save(requestDto.toReservationTime());
        return new ReservationTime(id, reservationTime);
    }

    public void deleteById(final long id) {
        if (reservationDao.checkExistReservationByTime(id)) {
            throw new RoomEscapeException(ExceptionMessage.EXIST_REFER_TIME);
        }
        reservationTimeDao.deleteById(id);
    }

    public List<ReservationUserTime> findAvailableTime(final String date, final long themeId) {
        return reservationTimeDao.findAvailableTime(date, themeId);
    }
}
