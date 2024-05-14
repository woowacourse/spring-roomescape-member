package roomescape.time.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.exception.RoomEscapeException;
import roomescape.reservation.dao.ReservationDao;
import roomescape.reservation.domain.ReservationDate;
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
            throw new RoomEscapeException("이미 해당 시간이 존재합니다.");
        }
        return reservationTimeDao.save(requestDto.toReservationTime());
    }

    public void deleteById(final long id) {
        if (reservationDao.checkExistReservationByTime(id)) {
            throw new RoomEscapeException("해당 시간을 예약한 예약내역이 존재하여 삭제가 불가합니다.");
        }
        reservationTimeDao.deleteById(id);
    }

    public List<ReservationUserTime> findAvailableTime(final String date, final long themeId) {
        final ReservationDate reservationDate = new ReservationDate(date);
        if (reservationDate.isBeforeToday()) {
            return reservationTimeDao.findAll().stream()
                    .map(reservationTime -> new ReservationUserTime(
                            reservationTime.getId(),
                            reservationTime.getStartAt().toString(),
                            true))
                    .toList();
        }
        return reservationTimeDao.findAvailableTime(date, themeId);
    }
}
