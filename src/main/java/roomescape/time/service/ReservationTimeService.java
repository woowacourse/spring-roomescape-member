package roomescape.time.service;

import java.util.List;

import org.springframework.stereotype.Service;

import roomescape.exception.DuplicatedDataException;
import roomescape.exception.EmptyDataAccessException;
import roomescape.exception.UnableDeleteDataException;
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
        final ReservationTime reservationTime = ReservationTime.createWithOutId(requestDto.startAt());
        if (reservationTimeDao.checkExistTime(reservationTime)) {
            throw new DuplicatedDataException("이미 해당 예약 시간이 존재합니다.");
        }
        final long id = reservationTimeDao.save(requestDto.toReservationTime());
        return ReservationTime.createWithId(id, reservationTime);
    }

    public void deleteById(final long id) {
        if (reservationDao.checkExistReservationByTime(id)) {
            throw new UnableDeleteDataException("해당 시간을 예약한 예약내역이 존재하여 삭제가 불가합니다.");
        }
        int affectedColumn = reservationTimeDao.deleteById(id);
        if (affectedColumn == 0) {
            throw new EmptyDataAccessException("timeId : %d에 해당하는 시간이 존재하지 않습니다.", id);
        }
    }

    public List<ReservationUserTime> findAvailableTime(final String date, final long themeId) {
        return reservationTimeDao.findAvailableTime(date, themeId);
    }
}
